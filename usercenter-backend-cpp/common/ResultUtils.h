#pragma once

#include "BaseResponse.h"

/**
 * 返回工具类
 */
template <class T>
class ResultUtils {
    public:
    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    static BaseResponse<T> susscess(T data){
        return new BaseResponse<T>(0,data,"ok");
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<T>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse error(ErrorCode errorCode,String message , String description){
        return new BaseResponse<T>(errorCode.getCode(),null,message,description);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse error(int errorCode,String message , String description){
        return new BaseResponse<T>(errorCode,null,message,description);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse<T>(errorCode.getCode(),null,errorCode.getMessage(),description);
    }
};
