#include <string>

#pragma once

/**
 * Redis常量类
 */
class RegexPatterns
{
public:
    /**
     * 手机号正则
     */
    static const std::string PHONE_REGEX;
    /**
     * 邮箱正则
     */
    static const std::string EMAIL_REGEX;
    /**
     * 密码正则。4~32位的字母、数字、下划线
     */
    static const std::string PASSWORD_REGEX;
    /**
     * 验证码正则, 6位数字或字母
     */
    static const std::string VERIFY_CODE_REGEX;
};

//const std::string RegexPatterns::PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
const std::string RegexPatterns::PHONE_REGEX = "^1(3|5|8)\\d{9}$";
const std::string RegexPatterns::EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
const std::string RegexPatterns::PASSWORD_REGEX = "^\\w{4,32}$";
const std::string RegexPatterns::VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";
