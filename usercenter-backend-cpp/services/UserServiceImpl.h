#pragma once

#include <memory>
#include <string>
#include <models/User.h>
#include <common/BaseResponse.h>
#include <common/ErrorCode.h>
#include <exception/BusinessException.h>
#include "UserService.h"
#include <models/User.h>

using namespace drogon;
using namespace drogon_model::testuser;
using namespace drogon::orm;

namespace usercenter
{

  class UserServiceImpl;
  using UserServiceImplPtr = std::shared_ptr<UserServiceImpl>;

  class UserServiceImpl : public UserService
  {
  public:
    UserServiceImpl()
    {
    }

    ~UserServiceImpl()
    {
    }

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(const std::string &userAccount, const std::string &userPassword, const std::string &checkPassword, const std::string &planetCode) override;

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(const std::string &userAccount, const std::string &userPassword, const HttpRequestPtr &request) override;


    /**
     * 用户注销
     * @param request
     * @return
     */
    long userLogout(const HttpRequestPtr &request) override;

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    std::vector<User> searchUsersByTags(std::vector<std::string> tagNameList) override;

    std::vector<User> userSearch(const std::string &username) override;

    User userCurrent(long id) override;

    bool userDelete(long id) override;

  private:
    User getSafetyUser(User originUser);
    bool checkSpecialCharacter(const std::string &str);
    std::string encryptPwd(const std::string &str);

  private:
     Mapper<User> userMapper = Mapper<User>(app().getDbClient());
  };
}