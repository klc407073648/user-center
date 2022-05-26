#pragma once

#include <iostream>
#include <string>
#include <common/ErrorCode.h>

template <class T>
class BaseResponse
{
public:
    BaseResponse(int code, T data, const std::string &message, const std::string &description)
    :code_(code),data_(data),message_(message),description_(description)
    {
    }

    BaseResponse(int code, T data)
    :code_(code),data_(data)
    {
        this.message_ = "";
        this.description_ = "";
    }

    BaseResponse(int code, T data, const std::string &message)
    :code_(code),data_(data),message_(message)
    {
        this(code, data, message, "");
        this.description_ = "";
    }

    BaseResponse(ErrorCode errorCode)
    {
        this.code_ = errorCode.getCode();
        this.data_ = NULL;
        this.message_ = errorCode.getMessage();
        this.description_ = errorCode.getDescription();
    }

private:
    int code_;
    T data_;
    std::string message_;
    std::string description_;
};