#include "UserServiceImpl.h"
#include <trantor/utils/Utilities.h>
#include <constants/RegexPatterns.h>
#include <constants/RedisConstants.h>
#include <regex>

using namespace usercenter;
using namespace drogon::internal;

long UserServiceImpl::sendCode(const std::string &phone, const HttpRequestPtr &request)
{
    // 1.校验手机号
    if(!isPhoneInvalid(phone)){
        // 2.如果不符合，返回错误信息
        throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号格式不对");
    }

    // 3.符合，生成验证码
    std::string code = "123456";

    // 4.保存验证码到 session
	std::string key = RedisConstants::LOGIN_USER_KEY + phone;

    redisClient->execCommandAsync(
        [](const drogon::nosql::RedisResult &r) {},
        [](const std::exception &err) {
            LOG_ERROR << "something failed!!! " << err.what();
        },
    "SETEX %s %d %s", key.c_str(),RedisConstants::LOGIN_CODE_TTL * 60,code.c_str());
	
    //stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

    // 5.发送验证码
    LOG_INFO << "发送短信验证码成功，验证码：" << code;
    // 返回ok

    return 123456;
}

/*
public Result login(LoginFormDTO loginForm, HttpSession session) {
        // 1.校验手机号
        String phone = loginForm.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }
        // 3.从redis获取验证码并校验
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        String code = loginForm.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.fail("验证码错误");
        }

        // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
        User user = query().eq("phone", phone).one();

        // 5.判断用户是否存在
        if (user == null) {
            // 6.不存在，创建新用户并保存
            user = createUserWithPhone(phone);
        }

        // 7.保存用户信息到 redis中
        // 7.1.随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // 7.2.将User对象转为HashMap存储
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        // 7.3.存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 7.4.设置token有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 8.返回token
        return Result.ok(token);
    }
*/

TbUser UserServiceImpl::login(const HttpRequestPtr &request)
{
}

long UserServiceImpl::logout(const HttpRequestPtr &request)
{
}

TbUser UserServiceImpl::getCurrent(const HttpRequestPtr &request)
{
}

bool UserServiceImpl::isPhoneInvalid(const std::string &str)
{
    //特殊字符校验
    //std::regex vaildPattern("^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$");

    std::regex vaildPattern(RegexPatterns::PHONE_REGEX);
    std::smatch match;

    if (regex_search(str, match, vaildPattern))
    {
        return true;
    }

    return false;
}
