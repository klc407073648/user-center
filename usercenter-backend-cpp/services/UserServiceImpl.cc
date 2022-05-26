#include "UserServiceImpl.h"
#include <drogon/HttpController.h>
#include <regex>

using namespace usercenter;

long UserServiceImpl::userRegister(const std::string &userAccount, const std::string &userPassword, const std::string &checkPassword, const std::string &planetCode)
{
	LOG_INFO << "UserServiceImpl::userRegister in";
    /*
    校验用户的账户、密码、校验密码，是否符合要求
    1. 非空
    2. 账户长度 **不小于** 4 位
    3. 密码就 **不小于** 8 位吧
    4. 账户不能重复
    5. 账户不包含特殊字符
    6. 密码和校验密码相同
    */
    try
    {
        if (userAccount.size() == 0 || userPassword.size() == 0 || checkPassword.size() == 0)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "参数为空");
        }

        if (userAccount.length() < 4)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "用户账号小于4位");
        }

        if (userPassword.length() < 8 || checkPassword.length() < 8)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "用户密码和校验密码小于8位");
        }

        if (planetCode.length() > 5)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "星球编号大于5位");
        }

        //特殊字符校验
        std::regex vaildPattern("[~!/@#$%^&*()\\-_=+\\|[{}];:\'\",<.>/?]+");
        if (regex_match(userAccount, vaildPattern))
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "用户账号中存在特殊字符");
        }

        //密码和校验密码相同
        if (userPassword!=checkPassword)
        {
            throw BusinessException(ErrorCode::PARAMS_ERROR(), "用户密码和校验密码不同");
        }

        /*
        //用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号已存在");
        }

        //星球账号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        if (count > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球账号已存在");
        }

        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);


        // 3.插入数据
        User user = new User();
        user.setIphone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean res = this.save(user);
        if (!res)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "插入数据失败");
        }


        return user.getId();
        */
    }
    catch (std::exception& ex)
    {
		LOG_INFO << "BusinessException error";
		LOG_INFO << ex.what();
		return -1;
    }
    return 1;
}
User UserServiceImpl::userLogin(const std::string &userAccount, const std::string &userPassword, const HttpRequestPtr &request)
{
}

User UserServiceImpl::getSafetyUser(User originUser)
{
}

int UserServiceImpl::userLogout(const HttpRequestPtr &request)
{
}
std::vector<User> UserServiceImpl::searchUsersByTags(std::vector<std::string> tagNameList)
{
}