#pragma once

#include <drogon/HttpController.h>

using namespace drogon;

namespace api
{
class user : public drogon::HttpController<user>
{
  public:
    METHOD_LIST_BEGIN
    // use METHOD_ADD to add your custom processing function here;
    METHOD_ADD(user::userRegister, "/register", Post);                  //path is /api/v1/User/{arg1}
    METHOD_ADD(user::userLogin, "/login", Post);  //path is /api/v1/User/{arg1}/detailinfo
    METHOD_ADD(user::userLogout, "/logout", Post);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(user::searchUsers, "/search", Get);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(user::getCurrentUser, "/current", Get);                 //path is /api/v1/User/{arg1}
    METHOD_ADD(user::deleteUsers, "/delete", Post);                 //path is /api/v1/User/{arg1}

    METHOD_LIST_END

    void userRegister(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void userLogin(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void userLogout(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void searchUsers(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void getCurrentUser(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
    void deleteUsers(const HttpRequestPtr &req, std::function<void(const HttpResponsePtr &)> &&callback);
	
	 public:
    user()
    {
        LOG_DEBUG << "User constructor!";
    }
  
};
}
