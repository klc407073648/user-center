#pragma once

#include <iostream>
#include <string>
#include <vector>
#include <json/json.h>
#include "BaseResponse.h"

/**
 * @brief Response2json 模板类
 *
 * @tparam T
 */
template <class T>
class Response2json
{
public:
    static Json::Value rep2json(BaseResponse<T> rep)
    {
        Json::Value ret;
        ret["code"] = rep.getCode();
        ret["data"] = static_cast<Json::Int64>(rep.getData());
        ret["message"] = rep.getMessage();
        ret["description"] = rep.getDescription();
        return ret;
    }
};

/**
 * @brief  Response2json 全特化 User
 *
 * @tparam
 */
template <>
class Response2json<TbUser>
{
public:
    static Json::Value rep2json(BaseResponse<TbUser> rep)
    {
        Json::Value ret;
        ret["code"] = rep.getCode();
        ret["data"] = rep.getData().toJson();
        ret["message"] = rep.getMessage();
        ret["description"] = rep.getDescription();
        return ret;
    }
};

/**
 * @brief  Response2json 全特化 std::vector<TbUser>
 *
 * @tparam
 */
template <>
class Response2json<std::vector<TbUser>>
{
public:
    static Json::Value rep2json(BaseResponse<std::vector<TbUser>> rep)
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