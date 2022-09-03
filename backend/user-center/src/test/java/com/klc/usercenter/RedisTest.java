package com.klc.usercenter;
import java.util.Date;


import com.klc.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //增加
        valueOperations.set("str","val1");
        valueOperations.set("intval",1);
        valueOperations.set("dval",6.5);
        User user = new User();
        user.setId(1111L);
        user.setUsername("test");
        valueOperations.set("user",user);
        //查询
        Object str = valueOperations.get("str");
        Assertions.assertTrue("val1".equals((String)str));
        Object intval = valueOperations.get("intval");
        Assertions.assertTrue( 1 == (Integer) intval);
        Object dval = valueOperations.get("dval");
        Assertions.assertTrue(6.5 == (Double)dval);
        System.out.println(valueOperations.get("user"));
    }
}
