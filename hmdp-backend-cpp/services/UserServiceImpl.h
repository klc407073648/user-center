#pragma once

#include <memory>
#include <string>
#include <models/TbUser.h>
#include <common/BaseResponse.h>
#include <common/ErrorCode.h>
#include <exception/BusinessException.h>
#include <drogon/nosql/RedisClient.h>
#include "UserService.h"

using namespace drogon;
using namespace drogon::orm;
using namespace drogon_model::hmdp;

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

    long sendCode(const std::string &phone, const HttpRequestPtr &request) override;

    TbUser login(const HttpRequestPtr &request) override;

    long logout(const HttpRequestPtr &request) override;

    TbUser getCurrent(const HttpRequestPtr &request) override;


  private:
    bool isPhoneInvalid(const std::string &str); 

  private:
    Mapper<TbUser> userMapper = Mapper<TbUser>(app().getDbClient()); //对象持久化映射层,连接User对象和数据库
    nosql::RedisClientPtr redisClient = app().getRedisClient(); 
  };
}