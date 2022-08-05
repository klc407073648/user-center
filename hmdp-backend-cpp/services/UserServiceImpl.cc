#include "UserServiceImpl.h"
#include <trantor/utils/Utilities.h>
#include <constants/RedisConstants.h>
#include <drogon/nosql/RedisClient.h>
#include <utils/RegexUtils.h>
#include <utils/RandomUtils.h>

using namespace usercenter;
using namespace drogon::internal;
using namespace drogon::nosql;

std::string UserServiceImpl::sendCode(const std::string &phone)
{
    LOG_DEBUG << "发送短信验证码对应的手机号:" << phone;

    // 1.校验手机号
    if (!RegexUtils::isPhoneInvalid(phone))
    {
        // 2.如果不符合，返回错误信息
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号格式不对");
    }

    // 3.符合，生成验证码
    std::string code = RandomUtils::getRandomNumberStr(6);

    // 4.保存验证码到session
    std::string key = RedisConstants::LOGIN_USER_KEY + phone;

    redisClient.setex(key, RedisConstants::LOGIN_CODE_TTL, code);

    LOG_DEBUG << "保存验证码到session: " << key << "=" << code;

    // 5.发送验证码
    LOG_INFO << "发送短信验证码成功，验证码:" << code;
    // 返回ok

    return code;
}

std::string UserServiceImpl::login(const std::string &phone, const std::string &code)
{
    LOG_DEBUG << "短信验证码登陆, 手机号: " << phone << ", 验证码: " << code;

    // 1.校验手机号
    if (!RegexUtils::isPhoneInvalid(phone))
    {
        // 2.如果不符合，返回错误信息
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号格式不对");
    }

    // 3.从redis获取验证码并校验
    std::string key = RedisConstants::LOGIN_USER_KEY + phone;

    std::string cacheCode("");
    auto val = redisClient.get(key);

    if (val)
    {
        cacheCode = *val;
    }

    LOG_INFO << "Redis中存储的验证码: " << key << "=" << cacheCode;

    if (cacheCode == "" || cacheCode != code)
    {
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "验证码不一致");
    }

    // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
    auto userList = userMapper.findBy(Criteria(TbUser::Cols::_phone, CompareOperator::EQ, phone));
    TbUser user;

    LOG_DEBUG << "根据手机号查询的用户数量: " << userList.size();

    // 5.判断用户是否存在
    if (userList.size() == 0)
    {
        // 6.不存在，创建新用户并保存
        user = createUserWithPhone(phone);
    }
    else
    {
        user = getSafetyUser(userList[0]);
    }

    // 7.保存用户信息到 redis中
    // 7.1.随机生成token，作为登录令牌
    std::string token = utils::getUuid();

    // 7.2.将User对象转为HashMap存储
    UserDTO *userDTO = new UserDTO(&user);
    std::unordered_map<std::string, std::string> userMap = userDTO->getMapObject();

    // 7.3.存储
    std::string tokenKey = RedisConstants::LOGIN_USER_KEY + token;
    redisClient.hmset(tokenKey, userMap.begin(), userMap.end());

    // 7.4.设置token有效期
    redisClient.expire(tokenKey, RedisConstants::LOGIN_USER_TTL);

    LOG_INFO << "tokenKey=" << tokenKey;

    // 8.返回token
    return token;
}

long UserServiceImpl::logout(const HttpRequestPtr &request)
{
}

TbUser UserServiceImpl::getCurrent(const HttpRequestPtr &request)
{
    // 1.获取请求头中的token
    std::string token = request->getHeader("JSESSIONID");

    LOG_DEBUG << "获取请求头中的token:" << token;

    if (token == "")
    {
        throw BusinessException(ErrorCode::NO_LOGIN(), "用户未登陆");
    }

    // 2.基于TOKEN获取redis中的用户
    std::string key = RedisConstants::LOGIN_USER_KEY + token;
    std::unordered_map<std::string, std::string> userMap;
    redisClient.hgetall(key, std::inserter(userMap, userMap.begin()));

    // 3.判断用户是否存在
    if (userMap.size() == 0)
    {
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "用户不存在");
    }

    // 4.将查询到的hash数据转为UserDTO
    UserDTO *userDTO = new UserDTO(userMap);
    // 5.存在，保存用户信息到 ThreadLocal
    TbUser user;
    user.setId(userDTO->getId());
    user.setNickName(userDTO->getNickName());
    user.setIcon(userDTO->getIcon());
    // 6.刷新token有效期
    redisClient.expire(key, RedisConstants::LOGIN_USER_TTL);

    return user;
}

TbUser UserServiceImpl::createUserWithPhone(const std::string &phone)
{
    TbUser user;
    user.setPhone(phone);
    user.setNickName("user_" + RandomUtils::getRandomNumberStr(10));
    userMapper.insert(user);

    auto ret = userMapper.findBy(Criteria(TbUser::Cols::_phone, CompareOperator::EQ, phone));

    if (ret.size() != 1)
    {
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "插入数据失败");
    }

    return user;
}

TbUser UserServiceImpl::getSafetyUser(const TbUser& originUser)
{
    TbUser safetyUser;
    safetyUser.setId(originUser.getValueOfId());
    safetyUser.setPhone(originUser.getValueOfPhone());
    safetyUser.setNickName(originUser.getValueOfNickName());
    safetyUser.setIcon(originUser.getValueOfIcon());

    return safetyUser;
}
