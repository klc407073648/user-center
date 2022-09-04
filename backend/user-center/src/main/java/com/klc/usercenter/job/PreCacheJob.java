package com.klc.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klc.usercenter.mapper.UserMapper;
import com.klc.usercenter.model.domain.User;
import com.klc.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedissonClient redissonClient;

    //重点用户缓存
    List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 58 16 * * *")
    public void doCacheRecommendUser()  {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");

        try
        {
            if(lock.tryLock(0,30000,TimeUnit.MILLISECONDS)) {
                System.out.println("getLock:"+Thread.currentThread().getId());
                //重点用户缓存
                for (Long userId : mainUserList) {
                    //从数据库查询记录
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);

                    //拼接key
                    String redisKey = String.format("yupao:user:recommand:%s", userId);
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

                    try {
                        //写入缓存
                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis error:", e);
                    }
                }
            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
            log.error("yupao:precachejob:docache:lock error",e);
        }finally {
            //只能释放自己的锁
            if(lock.isHeldByCurrentThread()){
                System.out.println("unlock"+Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }
}
