package com.klc.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.klc.usercenter.common.BaseResponse;
import com.klc.usercenter.common.ErrorCode;
import com.klc.usercenter.common.ResultUtils;
import com.klc.usercenter.exception.BusinessException;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.model.domain.request.UserLoginRequest;
import com.klc.usercenter.model.domain.request.UserRegisterRequest;
import com.klc.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.klc.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.klc.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8000"},allowedHeaders = "*",methods = {},allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;
    
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest ==null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        String planetCode=userRegisterRequest.getPlanetCode();

        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        long id = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);

        return ResultUtils.susscess(id);
    }

    @PostMapping("login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest ==null){
            return null;
        }

        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }

        User user= userService.userLogin(userAccount, userPassword,request);
        return ResultUtils.susscess(user);

    }
    @PostMapping("logout")
    public BaseResponse<Integer> userLoginout(HttpServletRequest request){
        if(request ==null){
            return null;
        }

        Integer result =  userService.userLogout(request);
        return ResultUtils.susscess(result);

    }
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if(!userService.isAdmin(request)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper =new QueryWrapper<>();

        if(StringUtils.isNoneBlank(username)){
            queryWrapper.like("username",username);
        }

        List<User> userList = userService.list();
        List<User> retUserList =userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

        return ResultUtils.susscess(retUserList);
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersBytags(@RequestParam(required = false) List<String> tagNameList){
        if(CollectionUtils.isEmpty(tagNameList)){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        List<User> userList =userService.searchUsersByTags(tagNameList);
        return ResultUtils.susscess(userList);
    }

    @PostMapping("update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request){
        //1.校验参数是否为空
        if(user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);

        //2.用户是否有权限
        //3.更新用户
        int  result= userService.updateUser(user,loginUser);
        return ResultUtils.susscess(result);
    }


        @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User curentUser =(User) userObj;
        //仅管理员可以查询
        if(curentUser == null ){
            return ResultUtils.error(ErrorCode.NO_LOGIN);
        }

        long userId =curentUser.getId();
        User user = userService.getById(userId);
        User safeUser =userService.getSafetyUser(user);
        return ResultUtils.susscess(safeUser);
    }

    @PostMapping ("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody long id,HttpServletRequest request){
        if(!userService.isAdmin(request)){
            return  null;
        }

        if(id <=0) {
            return null;
        }

        Boolean ret = userService.removeById(id);

        return ResultUtils.susscess(ret);
    }

}
