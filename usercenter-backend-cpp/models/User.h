#pragma once

class User
{
public:
    User(){}
    ~User(){}
private:
    long id;                  //用户id
    std::string username;     //用户昵称
    std::string userAccount;  //账号
    std::string avatarUrl;    //用户头像
    int gender;               //性别
    std::string userPassword; //密码
    std::string iphone;       //电话
    std::string email;        //邮箱
    int userStatus;           //状态-0正常
    std::string createTime;   //创建时间
    std::string updateTime;   //更新时间
    int idDelete;             //是否删除
    int userRole;             //用户角色 0-普通用户 1-管理员
    std::string planetCode;   //星球编号
    std::string tags;         //标签列表json
};