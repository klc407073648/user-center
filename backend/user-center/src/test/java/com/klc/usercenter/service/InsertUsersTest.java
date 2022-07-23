package com.klc.usercenter.service;

import com.klc.usercenter.mapper.UserMapper;
import com.klc.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class InsertUsersTest {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    //CPU密集型：分配的核心线程数 = CPU - 1
    //IO 密集型：分配的核心线程数 可以大于 CPU核数
    private ExecutorService executorService = new ThreadPoolExecutor(60,1000,10000, TimeUnit.MINUTES,new ArrayBlockingQueue<>(10000));

    /**
     * 批量插入用户,每次插入一个,之前1000个插入耗费34秒
     */
    @Test
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        for(int i=0;i<INSERT_NUM;i++){
            User user = new User();
            user.setUsername("假用户");
            user.setUserAccount("fakeuser");
            user.setAvatarUrl("https://636f-codenav-8grj8px727565176-https://fastly.jsdelivr.net/npm/@vant/assets/weapp.svg");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setIphone("11111");
            user.setEmail("xxx@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("1111");
            user.setTags("[]");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 批量插入用户,利用UserService的saveBatch方法每次插入100个,耗时15秒
     */
    @Test
    public void doInsertUsersByBatch(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;
        List<User> userList = new ArrayList<>();
        for(int i=0;i<INSERT_NUM;i++){
            User user = new User();
            user.setUsername("假用户");
            user.setUserAccount("fakeuser");
            user.setAvatarUrl("https://636f-codenav-8grj8px727565176-https://fastly.jsdelivr.net/npm/@vant/assets/weapp.svg");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setIphone("11111");
            user.setEmail("xxx@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("1111");
            user.setTags("[]");
            userList.add(user);
        }

        userService.saveBatch(userList,100);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 并发批量插入用户,利用并发每次插入100个,耗时5秒
     */
    @Test
    public void doConcurrencyInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<Void>> futureList = new ArrayList<>();

        for(int i=0;i<10;i++){
            List<User> userList = new ArrayList<>();
            for(int j=0;j<100;j++){
                User user = new User();
                user.setUsername("假用户");
                user.setUserAccount("fakeuser");
                user.setAvatarUrl("https://636f-codenav-8grj8px727565176-https://fastly.jsdelivr.net/npm/@vant/assets/weapp.svg");
                user.setGender(0);
                user.setUserPassword("12345678");
                user.setIphone("11111");
                user.setEmail("xxx@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("1111");
                user.setTags("[]");
                userList.add(user);
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
                System.out.println("ThreadName:" + Thread.currentThread().getName());
                userService.saveBatch(userList,100);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
