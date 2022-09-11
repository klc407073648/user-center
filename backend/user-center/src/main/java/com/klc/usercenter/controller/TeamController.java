package com.klc.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klc.usercenter.common.BaseResponse;
import com.klc.usercenter.common.ErrorCode;
import com.klc.usercenter.common.ResultUtils;
import com.klc.usercenter.exception.BusinessException;
import com.klc.usercenter.model.domain.Team;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.model.dto.TeamQuery;
import com.klc.usercenter.model.request.TeamAddRequest;
import com.klc.usercenter.model.request.TeamJoinRequest;
import com.klc.usercenter.model.request.TeamUpdateRequest;
import com.klc.usercenter.model.vo.TeamUserVO;
import com.klc.usercenter.service.TeamService;
import com.klc.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 队伍接口
 */
@RestController
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8000"},allowedHeaders = "*",methods = {},allowCredentials = "true")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;

    @Resource
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        if (teamAddRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        Team team =new Team();
        BeanUtils.copyProperties(teamAddRequest,team);
        Long teamId = teamService.addTeam(team,loginUser);

        return ResultUtils.success(teamId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id) {
        if (id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍删除失败");
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest,loginUser);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"队伍更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(@RequestBody long id) {
        if (id <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if( team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(team);
    }

/*    @GetMapping("/list")
    public BaseResponse<List<Team>> listTeams(TeamQuery teamQuery) {
        if( teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();

        try {
            BeanUtils.copyProperties(team,teamQuery);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        List<Team> teamList = teamService.list(queryWrapper);
        return ResultUtils.success(teamList);
    }*/

    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery,HttpServletRequest request) {
        if( teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);

        List<TeamUserVO> teamList = teamService.listTeams(teamQuery,isAdmin );
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamsByPage(TeamQuery teamQuery) {
        if( teamQuery == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = new Team();

        try {
            BeanUtils.copyProperties(teamQuery,team);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        Page<Team> page = new Page<>(teamQuery.getPageNum(),teamQuery.getPageSize());
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest,HttpServletRequest request){
        if( teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest,loginUser);
        return ResultUtils.success(result);
    }
}
