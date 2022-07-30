#include <string>

#pragma once
/**
* Redis常量类
*/
class RedisConstants
{
public:
    //"const std::string" 类型的成员不能包含类内初始值设定项
    static const std::string LOGIN_CODE_KEY;
    static const long LOGIN_CODE_TTL = 2;

    static const std::string LOGIN_USER_KEY;
    static const long LOGIN_USER_TTL = 36000L;

    static const long CACHE_NULL_TTL = 2;

    static const long CACHE_SHOP_TTL = 30;
    static const std::string CACHE_SHOP_KEY;

    static const std::string LOCK_SHOP_KEY;
    static const long LOCK_SHOP_TTL = 10;

    static const std::string SECKILL_STOCK_KEY;
    static const std::string BLOG_LIKED_KEY;
    static const std::string FEED_KEY;
    static const std::string SHOP_GEO_KEY;
    static const std::string USER_SIGN_KEY;
};

const std::string RedisConstants::LOGIN_CODE_KEY = "login:code:";
const std::string RedisConstants::LOGIN_USER_KEY = "login:token:";
const std::string RedisConstants::CACHE_SHOP_KEY = "cache:shop:";
const std::string RedisConstants::LOCK_SHOP_KEY = "lock:shop:";
const std::string RedisConstants::SECKILL_STOCK_KEY = "seckill:stock:";
const std::string RedisConstants::BLOG_LIKED_KEY = "blog:liked:";
const std::string RedisConstants::FEED_KEY = "feed:";
const std::string RedisConstants::SHOP_GEO_KEY = "shop:geo:";
const std::string RedisConstants::USER_SIGN_KEY = "sign:";