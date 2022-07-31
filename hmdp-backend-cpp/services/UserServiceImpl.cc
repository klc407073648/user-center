#include "UserServiceImpl.h"
#include <trantor/utils/Utilities.h>
#include <constants/RedisConstants.h>
#include <drogon/nosql/RedisClient.h>
#include <utils/RegexUtils.h>
#include <utils/RandomUtils.h>
#include <map>
#include <dto/UserDTO.h>

using namespace usercenter;
using namespace drogon::internal;
using namespace drogon::nosql;

std::string UserServiceImpl::sendCode(const std::string &phone)
{
    // 1.校验手机号
    if (!RegexUtils::isPhoneInvalid(phone))
    {
        // 2.如果不符合，返回错误信息
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号格式不对");
    }

    // 3.符合，生成验证码
    std::string code = RandomUtils::getRandomNumberStr(6);

    // 4.保存验证码到 session
    std::string key = RedisConstants::LOGIN_USER_KEY + phone;

    redisClient->execCommandAsync(
        [](const drogon::nosql::RedisResult &r) {},
        [](const std::exception &err)
        {
            LOG_ERROR << "something failed!!! " << err.what();
        },
        "SETEX %s %d %s", key.c_str(), RedisConstants::LOGIN_CODE_TTL * 60, code.c_str());

    // stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

    // 5.发送验证码
    LOG_INFO << "发送短信验证码成功，验证码：" << code;
    // 返回ok

    return code;
}

std::string UserServiceImpl::login(const std::string &phone, const std::string &code)
{
    // 1.校验手机号
    if (!RegexUtils::isPhoneInvalid(phone))
    {
        // 2.如果不符合，返回错误信息
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号格式不对");
    }

    // 3.从redis获取验证码并校验
    std::string cacheCode;
	std::string key = RedisConstants::LOGIN_USER_KEY + phone;
    
    redisClient->execCommandAsync(
    [](const drogon::nosql::RedisResult &r) {
        if (r.isNil())
            LOG_INFO << "Cannot find variable associated with the key 'name'";
        else{
            LOG_INFO << "Name is " << r.asString();
			//cacheCode = r.asString();
		}
		 LOG_INFO << "Name is " << r.asString();
    },
    [](const std::exception &err) {
        LOG_ERROR << "something failed!!! " << err.what();
    },
    "get " + key);
     

    if (cacheCode == "" || cacheCode != code)
    {
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "验证码不一致");
    }

    // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
    auto users = userMapper.findBy(Criteria(TbUser::Cols::_phone, CompareOperator::EQ, phone));
    TbUser user;
    // 5.判断用户是否存在
    if (users.size() == 0)
    {
        // 6.不存在，创建新用户并保存
        user = createUserWithPhone(phone);
    }
    else
    {
        user = users[0];
    }

    // 7.保存用户信息到 redis中
    // 7.1.随机生成token，作为登录令牌
    std::string token = utils::getUuid();
    // 7.2.将User对象转为HashMap存储
    UserDTO *userDTO = new UserDTO(&user);
    std::map<std::string, std::string> userMap = userDTO->getMapObject();
    // 7.3.存储
    std::string tokenKey = RedisConstants::LOGIN_USER_KEY + token;
    LOG_DEBUG << "tokenKey: " << tokenKey;

    std::string execCmd = "hmset " + tokenKey;

    for (auto iter = userMap.begin(); iter != userMap.end(); iter++)
    {
        execCmd = execCmd + iter->first + " " + iter->second + " ";
    }

    redisClient->execCommandAsync(
        [](const drogon::nosql::RedisResult &r) {},
        [](const std::exception &err)
        {
            LOG_ERROR << "something failed!!! " << err.what();
        },
        execCmd);

    LOG_DEBUG << "execCmd: " << execCmd;

    // 7.4.设置token有效期
    redisClient->execCommandAsync(
        [](const drogon::nosql::RedisResult &r) {},
        [](const std::exception &err)
        {
            LOG_ERROR << "something failed!!! " << err.what();
        },
        "expire %s %d",
        tokenKey, RedisConstants::LOGIN_USER_TTL);

    // 8.返回token
    return token;
}

long UserServiceImpl::logout(const HttpRequestPtr &request)
{
}

TbUser UserServiceImpl::getCurrent(const HttpRequestPtr &request)
{
}

TbUser UserServiceImpl::createUserWithPhone(const std::string &phone)
{
    TbUser user;
    user.setPhone(phone);

    return user;
}
