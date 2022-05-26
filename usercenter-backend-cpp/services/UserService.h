#pragma once

#include <memory>
#include <string>
#include <vector>
#include <drogon/HttpController.h>
#include <models/User.h>
#include <vector>

using namespace drogon;

namespace usercenter
{
class UserService 
{
  public:
    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    virtual long userRegister(const std::string& userAccount, const std::string& userPassword, const std::string& checkPassword,const std::string& planetCode) = 0;

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    virtual User userLogin(const std::string& userAccount, const std::string& userPassword, const HttpRequestPtr &request) = 0;

    virtual User getSafetyUser(User originUser) = 0;

    /**
     * 用户注销
     * @param request
     * @return
     */
    virtual int userLogout(const HttpRequestPtr &request) = 0;

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    virtual std::vector<User> searchUsersByTags(std::vector<std::string> tagNameList) = 0;
  
};
}
