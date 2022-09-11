package com.klc.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klc.usercenter.common.ErrorCode;
import com.klc.usercenter.exception.BusinessException;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.service.UserService;
import com.klc.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.klc.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.klc.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Jason
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2022-04-24 22:17:56
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    //盐值混淆密码
    private static final String salt = "kkk";

    /**
     * 用户登录状态
     */

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //1. 校验
        if(StringUtils.isAllBlank(userAccount,userPassword,checkPassword,planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4位");
        }

        if(userPassword.length()<8 || checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码和校验密码小于8位");
        }

        if(planetCode.length()>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号大于5位");
        }

        //特殊字符校验
        String vaildPattern =  "[~!/@#$%^&*()\\-_=+\\|[{}];:\'\",<.>/?]+";
        Matcher matcher = Pattern.compile(vaildPattern).matcher(userAccount);
        if(matcher.find())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号中存在特殊字符");
        }

        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码和校验密码不同");
        }

        //用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count =userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号已存在");
        }

        //星球账号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode",planetCode);
        count =userMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球账号已存在");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt+userPassword).getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);

        //3.插入数据
        User user = new User();
        user.setIphone("");
        user.setEmail("");
        user.setUserStatus(0);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean res= this.save(user);
        if(!res){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"插入数据失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        if(StringUtils.isAllBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号或密码为空");
        }

        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号小于4位");
        }

        if(userPassword.length()<8 ){
            return null;
        }

        //特殊字符校验
        String vaildPattern =  "[~!/@#$%^&*()\\-_=+\\|[{}];:\'\",<.>/?]+";
        Matcher matcher = Pattern.compile(vaildPattern).matcher(userAccount);
        if(matcher.find())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号存在特殊字符");
        }

        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt+userPassword).getBytes(StandardCharsets.UTF_8));
        System.out.println(encryptPassword);

        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user =userMapper.selectOne(queryWrapper);

        //用户不存在
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }

        //3. 用户脱密
        User safetyUser = getSafetyUser(user);

        //4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱密
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser){
        if(originUser == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setIphone(originUser.getIphone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setTags(originUser.getTags());

        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户
     * @param tagNameList 用户拥有的标签
     * @return
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {

        if(CollectionUtils.isEmpty(tagNameList)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper queryWrapper = new QueryWrapper<>();
        //1.先查询所有用户
        List<User> userList = userMapper.selectList(queryWrapper);
        //2.在内存中判断是否包含要求标签
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
                String tagStr =user.getTags();
                if(StringUtils.isBlank(tagStr)){
                    return false;
                }
                Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>(){}.getType());

                for(String tagName: tagNameList){
                    if(!tempTagNameSet.contains(tagName)){
                        return  false;
                    }
                }
                return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public int updateUser(User user,User loginUser) {
        long userId = user.getId();
        if(userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //todo 补充校验，如果用户没有传任何要更新的值，直接报错，不用执行更新语句
        //### SQL: UPDATE user    WHERE id=?  AND idDelete=0
        //如果是管理员，允许更新任意用户
        //如果不是管理员，只需要更新当前用户信息
        if( !isAdmin(loginUser) && userId != loginUser.getId()){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        User oldUser = userMapper.selectById(userId);
        if(oldUser == null ){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return userMapper.updateById(user);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user =(User) userObj;
        //仅管理员可以查询
        if(user == null || user.getUserRole()!= ADMIN_ROLE){
            return  false;
        }

        return true;
    }

    /**
     * 是否管理员用户
     */
    @Override
    public boolean isAdmin(User loginUser){
        if(loginUser == null || loginUser.getUserRole()!= ADMIN_ROLE){
            return false;
        }
        return true;
    }

    /**
     * 是否管理员用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        if( request == null) {
            return null;
        }

        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if( userObj == null){
            throw  new BusinessException(ErrorCode.NO_AUTH);
        }

        return (User) userObj;
    }

    /**
     * 根据标签搜索用户(SQL版本查询)
     * @param tagNameList 用户拥有的标签
     * @return
     */
    @Deprecated
    private List<User> searchUsersByTagsSQL(List<String> tagNameList) {

        if(CollectionUtils.isEmpty(tagNameList)){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // and 方式查询标签
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        for(String tagName:tagNameList){
//            queryWrapper =queryWrapper.like("tags",tagName);
//        }
//
//        List<User> userList = userMapper.selectList(queryWrapper);

        QueryWrapper queryWrapper = new QueryWrapper<>();
        //1.先查询所有用户
        List<User> userList = userMapper.selectList(queryWrapper);
        //2.在内存中判断是否包含要求标签
        Gson gson = new Gson();
        return userList.stream().filter(user -> {
            String tagStr =user.getTags();
            if(StringUtils.isBlank(tagStr)){
                return false;
            }
            Set<String> tempTagNameSet = gson.fromJson(tagStr, new TypeToken<Set<String>>(){}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for(String tagName: tagNameList){
                if(!tempTagNameSet.contains(tagName)){
                    return  false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());

        //return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }
}




