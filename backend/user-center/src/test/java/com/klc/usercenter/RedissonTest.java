package com.klc.usercenter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klc.usercenter.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        List<String> list = new ArrayList<>();
        list.add("list1");
        System.out.println("list:" + list.get(0));
        //list
        RList<String> rList = redissonClient.getList("mylist");
        rList.add("list1");
        rList.get(0);
        //rList.remove(0);
        System.out.println("list:" + rList.get(0));
        //map
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        System.out.println("map:" + map);

        RMap<Object, Object> rmap = redissonClient.getMap("myMap");
        rmap.put("key1", 1);
        rmap.put("key2", 2);
        System.out.println("rmap:" + rmap);
        //set

        //stack
    }

    @Test
    void testWatchDog() {
        RLock lock = redissonClient.getLock("yupao:precachejob:docache:lock");

        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock:" + Thread.currentThread().getId());
                Thread.sleep(300000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("yupao:precachejob:docache:lock error", e);
        } finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unlock" + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

}
