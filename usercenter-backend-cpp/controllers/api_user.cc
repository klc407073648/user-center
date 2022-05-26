#include "api_user.h"
#include "services/UserServiceImpl.h"

using namespace api;
using namespace usercenter;

// Add definition of your processing function here

void user::userRegister(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::userRegister in";
	auto jsonPtr = req->jsonObject();

    std::string userAccount = (*jsonPtr)["userAccount"].asString();
    std::string userPassword = (*jsonPtr)["userPassword"].asString();
    std::string checkPassword = (*jsonPtr)["checkPassword"].asString();
    std::string planetCode = (*jsonPtr)["planetCode"].asString();
    std::string email = (*jsonPtr)["email"].asString();

	LOG_INFO << userAccount;
	
    Json::Value user;
    user["userAccount"] = userAccount;
    user["userPassword"] = userPassword;
    user["checkPassword"] = checkPassword;
    user["planetCode"] = planetCode;
    user["email"] = email;

    Json::Value json;
    json["code"] = 200;
    json["data"] = user;
    json["message"] = "ok";
    json["description"] = "ok";
	json["object"] = *jsonPtr;
	
	UserServiceImpl* userImpl = new UserServiceImpl();
	userImpl->userRegister(userAccount,userPassword,checkPassword,planetCode);
    auto resp = HttpResponse::newHttpJsonResponse(json);
    callback(resp);
}

void user::userLogin(const HttpRequestPtr &req,
                        std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::userLogin in";
}

void user::userLogout(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::userLogout in";
}

void user::searchUsers(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::searchUsers in";
}

void user::getCurrentUser(const HttpRequestPtr &req,
                   std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::getCurrentUser in";
}

void user::deleteUsers (
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "user::deleteUsers in";
}

