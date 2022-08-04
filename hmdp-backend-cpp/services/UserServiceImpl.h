#pragma once

#include <memory>
#include <string>
#include <models/TbUser.h>
#include <common/BaseResponse.h>
#include <common/ErrorCode.h>
#include <exception/BusinessException.h>
#include <drogon/nosql/RedisClient.h>
#include "UserService.h"
#include <sw/redis++/redis++.h>

using namespace drogon;
using namespace drogon::orm;
using namespace drogon_model::hmdp;
using namespace sw::redis;

namespace usercenter
{
  class UserServiceImpl : public UserService
  {
  public:
    UserServiceImpl()
    {
    }

    ~UserServiceImpl()
    {
    }

    std::string sendCode(const std::string &phone) override;

    std::string login(const std::string &phone, const std::string &code) override;

    long logout(const HttpRequestPtr &request) override;

    TbUser getCurrent(const HttpRequestPtr &request) override;

  private:
    TbUser createUserWithPhone(const std::string &phone);
    TbUser getSafetyUser(TbUser originUser);

  private:
    Mapper<TbUser> userMapper = Mapper<TbUser>(app().getDbClient()); //对象持久化映射层,连接User对象和数据库
    Redis redisClient = Redis("tcp://456789@81.68.132.31:6380");      // Redis连接对象
  };
}