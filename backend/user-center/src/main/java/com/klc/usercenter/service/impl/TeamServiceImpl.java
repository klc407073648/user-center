package com.klc.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klc.usercenter.common.ErrorCode;
import com.klc.usercenter.exception.BusinessException;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.model.domain.UserTeam;
import com.klc.usercenter.model.dto.TeamQuery;
import com.klc.usercenter.model.enums.TeamStatusEnum;
import com.klc.usercenter.model.request.TeamJoinRequest;
import com.klc.usercenter.model.request.TeamUpdateRequest;
import com.klc.usercenter.model.vo.TeamUserVO;
import com.klc.usercenter.model.vo.UserVO;
import com.klc.usercenter.service.TeamService;
import com.klc.usercenter.model.domain.Team;
import com.klc.usercenter.mapper.TeamMapper;
import com.klc.usercenter.service.UserService;
import com.klc.usercenter.service.UserTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
* @author Jason
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2022-09-05 07:35:05
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class )
    public long addTeam(Team team, User loginUser) {
        //1. 请求参数是否为空？
        if( team ==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = loginUser.getId();
        //2. 是否登录，未登录不允许创建
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        //3. 校验信息
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);

        //1. 队伍人数 > 1 且 <= 20
        if(!( maxNum > 1 && maxNum <=20)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }

        //2. 队伍标题 <= 20
        String name =team.getName();

        if(StringUtils.isBlank(name) || name.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不满足要求");
        }
        //3. 描述 <= 512
        String description =team.getDescription();

        if(StringUtils.isNotBlank(description) && description.length()>512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述过长");
        }
        //4. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);

        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if( statusEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态不满足要求");
        }
        //5. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password =team.getPassword();
        if(TeamStatusEnum.SECRET.equals(statusEnum)){
            if(StringUtils.isBlank(password) || password.length() >32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //6. 超时时间 > 当前时间
        Date expireTime = team.getExpireTime();
        Date now =new Date();
        if(now.after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 > 当前时间");
        }
        //7. 校验用户最多创建 5 个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long hasTeamNum =this.count(queryWrapper);

        if( hasTeamNum >= 5 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建5个队伍");
        }
        //8. 插入队伍信息到队伍表
        team.setId(null);//让系统自动生成id
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();

        if( !result || teamId == null ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }

        //9. 插入用户  => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());

        result = userTeamService.save(userTeam);

        if( !result ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> listTeams(TeamQuery teamQuery,boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        //1. 从请求参数中取出队伍名称等查询条件，如果存在则作为查询条件
        //组合查询条件
        if( teamQuery != null){
            Long id = teamQuery.getId();

            //查询id
            if(id!=null && id > 0) {
                queryWrapper.eq("id", id);
            }

            //根据搜索关键词查询
            String searchText = teamQuery.getSearchText();
            if(StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name",searchText).or().like("description",searchText));
            }

            //根据包含队伍名称查询
            String name = teamQuery.getName();
            if(StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }

            //根据包含描述信息查询
            String description = teamQuery.getDescription();
            if(StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }

            //根据最大人数相等查询
            Integer maxNum = teamQuery.getMaxNum();
            if(maxNum!=null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }

            //根据创建人查询
            Long userId = teamQuery.getUserId();
            if(userId!=null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }

            //根据状态查询
            Integer status = teamQuery.getStatus();
            TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);

            if(statusEnum == null){
                statusEnum =TeamStatusEnum.PUBLIC;
            }

            if(!isAdmin && !statusEnum.equals(TeamStatusEnum.PUBLIC)){
                throw new BusinessException(ErrorCode.NO_AUTH);
            }

            queryWrapper.eq("status", statusEnum.getValue());
        }

        //不展示已过期的队伍（根据过期时间筛选）
        // expireTime is null or expireTime > now()
        queryWrapper.and(qw -> qw.isNull("expireTime").or().gt("expireTime",new Date()));

        List<Team> teamList = this.list(queryWrapper);
        List<TeamUserVO> teamUserVOList =new ArrayList<>();

        if(CollectionUtils.isEmpty(teamList)){
            return  new ArrayList<>();
        }
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if( userId == null){
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            //脱敏用户信息
            if( user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }

        return teamUserVOList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser) {
        if( teamUpdateRequest == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();

        if( id ==null || id<=0){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Team oldTeam = this.getById(id);

        if( oldTeam ==null ){
            throw  new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }

        //只有管理员或者队伍的创建者可以修改
        boolean isAdminValue =userService.isAdmin(loginUser);
        if ( oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser) ){
            throw  new BusinessException(ErrorCode.NO_AUTH);
        }

        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());

        if(statusEnum.equals(TeamStatusEnum.SECRET)){
            if(StringUtils.isBlank( teamUpdateRequest.getPassword())){
                throw  new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态为加密，需要设置加密密码");
            }
        }

        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest,updateTeam);
        boolean result = this.updateById(updateTeam);

        return result;
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser) {
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        //队伍必须存在，只能加入未满、未过期的队伍
        Long teamId = teamJoinRequest.getTeamId();
        if( teamId == null || teamId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Team team = this.getById(teamId);
        if( team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }

        Date expireTime = team.getExpireTime();
        Date now = new Date();
        if( expireTime != null && expireTime.before(now)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已过期");
        }
        //3. 不能加入自己的队伍，不能重复加入已加入的队伍（幂等性）
        if(team.getUserId() == loginUser.getId()){

        }
        //4. 禁止加入私有的队伍
        Integer status = team.getStatus();
        TeamStatusEnum teamStatusEnum = TeamStatusEnum.getEnumByValue(status);
        if( TeamStatusEnum.PRIVATE.equals(teamStatusEnum)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"禁止加入私有队伍");
        }

        //5. 如果加入的队伍是加密的，必须密码匹配才可以
        String password = teamJoinRequest.getPassword();
        if( TeamStatusEnum.SECRET.equals(teamStatusEnum )) {
            if(StringUtils.isBlank(password) || !password.equals(team.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍是加密,但是密码不匹配");
            }
        }

        //尽量把数据库操作放在后面
        //用户最多加入 5 个队伍
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        long hasJoinNum = userTeamService.count(userTeamQueryWrapper);

        if(hasJoinNum>5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多创建和加入队伍为5个");
        }

        //不能重复加入已加入的队伍
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId",userId);
        userTeamQueryWrapper.eq("teamId",teamId);
        long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
        if(hasUserJoinTeam> 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已加入该队伍");
        }

        //已加入队伍的数量
        userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId",teamId);
        long teamHasJoinNum = userTeamService.count(userTeamQueryWrapper);

        if(teamHasJoinNum>=team.getMaxNum()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"加入的队伍已满");
        }
        //新增队伍 - 用户关联信息
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());

        return userTeamService.save(userTeam);
    }


}




