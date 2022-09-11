package com.klc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.klc.usercenter.model.domain.Team;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.model.dto.TeamQuery;
import com.klc.usercenter.model.request.TeamJoinRequest;
import com.klc.usercenter.model.request.TeamUpdateRequest;
import com.klc.usercenter.model.vo.TeamUserVO;

import java.util.List;

/**
* @author Jason
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2022-09-05 07:35:05
*/
public interface TeamService extends IService<Team> {


    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);


    /**
     * 搜索队伍
     * @param teamQuery
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);


    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);
}
