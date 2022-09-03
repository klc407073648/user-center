package com.klc.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klc.usercenter.mapper.UserMapper;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 预热缓存Job
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService ;

    @Resource
    private RedisTemplate redisTemplate;

    //重点用户缓存
    List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 56 23 * * *")
    public void doCacheRecommendUser(){
        //重点用户缓存
        for(Long userId:mainUserList) {
            //从数据库查询记录
            QueryWrapper<User> queryWrapper =new QueryWrapper<>();
            Page<User>  userPage = userService.page(new Page<>(1,20),queryWrapper);

            //拼接key
            String redisKey = String.format("yupao:user:recommand:%s", userId);
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

            try {
                //写入缓存
                valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
            } catch(Exception e){
                log.error("redis error:",e);
            }
        }

    }
}
