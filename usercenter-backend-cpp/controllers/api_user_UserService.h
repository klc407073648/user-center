#pragma once

#include <drogon/HttpController.h>

using namespace drogon;

namespace api
{
namespace user
{
class UserService : public drogon::HttpController<UserService>
{
  public:
    METHOD_LIST_BEGIN
    // use METHOD_ADD to add your custom processing function here;
    METHOD_ADD(UserService::userRegister, "/register", Post);                  //path is /api/v1/User/{arg1}
    METHOD_ADD(UserService::userLogin, "/login", Post);  //path is /api/v1/User/{arg1}/detailinfo
    METHOD_ADD(UserService::userLogout, "/logout", Post);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(UserService::searchUsers, "/search", Get);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(UserService::getCurrentUser, "/current", Get);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(UserService::deleteUsers, "/delete", Post);                 //path is /api/v1/User/{arg1}

    METHOD_LIST_END

    void userRegister(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void userLogin(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void userLogout(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void searchUsers(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void getCurrentUser(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void deleteUsers(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
	
	 public:
    UserService()
    {
        LOG_DEBUG << "User constructor!";
    }
  
};
}
}
