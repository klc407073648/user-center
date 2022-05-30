#pragma once

#include <common/BaseResponse.h>
#include <json/json.h>
#include <string>
#include <vector>

/**
 * 返回工具类
 */
template <class T>
class ResultUtils
{
public:
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    static BaseResponse<T> susscess(T data)
    {
        return BaseResponse<T>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse<T> error(ErrorCode errorCode)
    {
        return BaseResponse<T>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse<T> error(ErrorCode errorCode, const std::string &message, const std::string &description)
    {
        return BaseResponse<T>(errorCode.getCode(), NULL, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse<T> error(int errorCode, const std::string &message, const std::string &description)
    {
        return BaseResponse<T>(errorCode, NULL, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse<T> error(ErrorCode errorCode, const std::string &description)
    {
        return BaseResponse<T>(errorCode.getCode(), NULL, errorCode.getMessage(), description);
    }

    /**
     * rep2json
     * @param errorCode
     * @return
     */
    static Json::Value rep2json(BaseResponse<T> rep)
    {
        Json::Value ret;
        ret["code"] = rep.getCode();
        ret["data"] = rep.getData();
        ret["message"] = rep.getMessage();
        ret["description"] = rep.getDescription();
        return ret;
    }

    static Json::Value rep2jsonUser(BaseResponse<User> rep)
    {
        Json::Value ret;
        ret["code"] = rep.getCode();
        ret["data"] = rep.getData().toJson();
        ret["message"] = rep.getMessage();
        ret["description"] = rep.getDescription();
        return ret;
    }

    static Json::Value rep2jsonUserList(BaseResponse<std::vector<User>> rep)
    {
        Json::Value arr;
        for (auto user : rep.getData())
        {
            arr.append(user.toJson());
        }

        Json::Value ret;
        ret["code"] = rep.getCode();
        ret["data"] = arr;
        ret["message"] = rep.getMessage();
        ret["description"] = rep.getDescription();
        return ret;
    }
};
