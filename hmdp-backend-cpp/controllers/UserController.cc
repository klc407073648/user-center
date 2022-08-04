#include "UserController.h"

void UserController::userCode(const HttpRequestPtr &request, std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::userCode in";

    std::string phone = ((*(request->getJsonObject()))["phone"]).asString();

    try
    {
        if (phone.size() == 0)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号为空");
        }

        std::string code = userSrvPtr_->sendCode(phone);

        auto base = ResultUtils<std::string>::susscess(code);
        auto json = Response2json<std::string>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
    catch (BusinessException &e)
    {
        LOG_INFO << "BusinessException error: message:" << e.what() << ",description" << e.getDescription();
        auto base = ResultUtils<long>::error(e.getCode(), e.getMessage(), e.getDescription());
        auto json = Response2json<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
}

void UserController::userLogin(const HttpRequestPtr &request, std::function<void(const HttpResponsePtr &)> &&callback)
{
    LOG_INFO << "UserController::userLogin in";

    std::string phone = ((*(request->getJsonObject()))["phone"]).asString();
    std::string code = ((*(request->getJsonObject()))["code"]).asString();

    try
    {
        if (phone.size() == 0 || code.size() == 0)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "入参中手机号或验证码为空");
        }

        std::string token = userSrvPtr_->login(phone, code);

        auto base = ResultUtils<std::string>::susscess(token);
        auto json = Response2json<std::string>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
    catch (BusinessException &e)
    {
        LOG_INFO << "BusinessException error: message:" << e.what() << ",description" << e.getDescription();
        auto base = ResultUtils<long>::error(e.getCode(), e.getMessage(), e.getDescription());
        auto json = Response2json<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
}
void UserController::userLogout(const HttpRequestPtr &request, std::function<void(const HttpResponsePtr &)> &&callback) {}
void UserController::userCurrent(const HttpRequestPtr &request, std::function<void(const HttpResponsePtr &)> &&callback) 
{
    LOG_INFO << "UserController::userCurrent in";

    try
    {
        TbUser tbUser= userSrvPtr_->getCurrent(request);

        auto base = ResultUtils<TbUser>::susscess(tbUser);
        auto json = Response2json<TbUser>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
    catch (BusinessException &e)
    {
        LOG_INFO << "BusinessException error: message:" << e.what() << ",description" << e.getDescription();
        auto base = ResultUtils<long>::error(e.getCode(), e.getMessage(), e.getDescription());
        auto json = Response2json<long>::rep2json(base);
        auto resp = HttpResponse::newHttpJsonResponse(json);
        callback(resp);
    }
}