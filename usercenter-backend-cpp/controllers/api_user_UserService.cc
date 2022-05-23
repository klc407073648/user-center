#include "api_user_UserService.h"
#include <drogon/drogon.h>

using namespace api::user;

// Add definition of your processing function here

void UserService::userRegister(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "UserService::userRegister in";
	
    std::string userAccount = req->getParameter("userAccount");
    std::string userPassword = req->getParameter("userPassword");
    std::string checkPassword = req->getParameter("checkPassword");
    std::string planetCode = req->getParameter("planetCode");

    Json::Value json;
    json["code"] = "200";
    json["data"] = "1";
    json["message"] = "ok";
    json["description"] = "ok";
    auto resp = HttpResponse::newHttpJsonResponse(json);
    callback(resp);
}

void UserService::userLogin(const HttpRequestPtr &req,
                        std::function<void(const HttpResponsePtr &)> &&callback)
{
}

void UserService::userLogout(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
}

void UserService::searchUsers(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
	LOG_INFO << "UserService::searchUsers in";
}

void UserService::getCurrentUser(const HttpRequestPtr &req,
                   std::function<void(const HttpResponsePtr &)> &&callback)
{
}

void UserService::deleteUsers (
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
}