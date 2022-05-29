#include "UserController.h"
#include <common/ResultUtils.h>
#include <models/User.h>

using namespace usercenter;

// Add definition of your processing function here
namespace drogon
{
    /**
     * @brief This template is used to convert a request object to a custom
     * type object. Users must specialize the template for a particular type.
     */
    template <>
    inline User fromRequest(const HttpRequest &req)
    {
        auto json = req.getJsonObject();
        auto userJson = (*json);
        auto user = User(userJson);
        return user;
    }
}

void UserController::userRegister(const HttpRequestPtr &req,std::function<void(const HttpResponsePtr &)> &&callback, User &&reqUser)
{
    LOG_INFO << "UserController::userRegister in";

    std::string checkPassword = ((*(req->getJsonObject()))["checkPassword"]).asString();

	LOG_INFO << "checkPassword:" << checkPassword;
	
    if (reqUser.getUseraccount() == nullptr || reqUser.getUserpassword() == nullptr || checkPassword == "" || reqUser.getPlanetcode() == nullptr )
    {
        auto base = ResultUtils<long>::error(ErrorCode::PARAMS_ERROR());
        auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
        return;
    }

    std::string userAccount = reqUser.getValueOfUseraccount();
    std::string userPassword = reqUser.getValueOfUserpassword();
    // std::string checkPassword = checkPassword_;
    std::string planetCode = reqUser.getValueOfPlanetcode();

    if (userAccount.size() == 0 || userPassword.size() == 0 || checkPassword.size() == 0)
    {
        auto base = ResultUtils<long>::error(ErrorCode::PARAMS_ERROR());
        auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
        return;
    }
	
	long id ;
	try{
		id = userSrvPtr_->userRegister(userAccount, userPassword, checkPassword, planetCode);
	}
	catch (BusinessException& e)
    {
		LOG_INFO << "BusinessException error";
		LOG_INFO << e.what();
		LOG_INFO << e.getDescription();
		auto base = ResultUtils<long>::error(e.getCode(),e.getMessage(),e.getDescription());
		auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
		return;
		//return -1;
    }

    auto base = ResultUtils<long>::susscess(id);
    auto json = ResultUtils<long>::rep2json(base);
    auto resp = HttpResponse::newHttpJsonResponse(json);
    callback(resp);
}

void UserController::userLogin(const HttpRequestPtr &req,std::function<void(const HttpResponsePtr &)> &&callback, User &&reqUser)
{
    LOG_INFO << "UserController::userLogin in";

    if (reqUser.getUseraccount() == nullptr || reqUser.getUserpassword() == nullptr )
    {
        auto base = ResultUtils<long>::error(ErrorCode::PARAMS_ERROR());
        auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
        return;
    }

    std::string userAccount = reqUser.getValueOfUseraccount();
    std::string userPassword = reqUser.getValueOfUserpassword();

    if (userAccount.size() == 0 || userPassword.size() == 0)
    {
        auto base = ResultUtils<long>::error(ErrorCode::PARAMS_ERROR());
        auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
        return;
    }

	User user;
	try{
		 user= userSrvPtr_->userLogin(userAccount, userPassword,req);
	}
	catch (BusinessException& e)
    {
		LOG_INFO << "BusinessException error";
		LOG_INFO << e.what();
		LOG_INFO << e.getDescription();
		auto base = ResultUtils<long>::error(e.getCode(),e.getMessage(),e.getDescription());
		auto json = ResultUtils<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
		return;
		//return -1;
    }
    
    auto base = ResultUtils<User>::susscess(user);
    auto json = ResultUtils<User>::rep2jsonUser(base);
    auto resp = HttpResponse::newHttpJsonResponse(json);
    callback(resp);
}

void UserController::userLogout(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::userLogout in";
}

void UserController::searchUsers(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::searchUsers in";
}

void UserController::getCurrentUser(const HttpRequestPtr &req,
                                    std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::getCurrentUser in";
}

void UserController::deleteUsers(
    const HttpRequestPtr &req,
    std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::deleteUsers in";
}
