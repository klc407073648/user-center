package com.klc.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klc.usercenter.service.UserTeamService;
import com.klc.usercenter.model.domain.UserTeam;
import com.klc.usercenter.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author Jason
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2022-09-05 07:37:32
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {

}




