package com.klc.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.klc.usercenter.model.domain.Team;
import com.klc.usercenter.model.domain.User;

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
}
