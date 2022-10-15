package com.klc.usercenter.service;

import com.klc.usercenter.common.BaseResponse;
import com.klc.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.klc.usercenter.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.klc.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.klc.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Jason
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2022-04-24 22:17:56
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode);

    /**
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 更新用户
     * @param user
     * @return
     */
    int updateUser(User user,User loginUser);


    /**
     * 从request的cookie中获取当前登陆用户信息
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 匹配用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);
}
