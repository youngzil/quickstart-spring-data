/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：TestRedis2.java
 * 版本信息：
 * 日期：2017年11月28日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TestRedis2
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月28日 上午11:38:07
 * @since 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
@DirtiesContext
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// 三种执行顺序可供选择：默认（MethodSorters.DEFAULT），按方法名（MethodSorters.NAME_ASCENDING）和JVM（MethodSorters.JVM）
public class TestRedis2 {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(String key, String value) {
        /*redisTemplate.opsForList(); 
        redisTemplate.opsForSet(); 
        redisTemplate.opsForHash()*/
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        valueOper.set(key, value);
    }

    @Test
    public void read() {
        String key = "order";
        String value = "hahahah";

        save(key, value);

        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        String getValue = valueOper.get(key);
        Assert.assertEquals(value, getValue);

        delete(key);
        String getValue2 = valueOper.get(key);
        Assert.assertEquals(value, getValue2);

    }

    public void delete(String key) {
        ValueOperations<String, String> valueOper = redisTemplate.opsForValue();
        RedisOperations<String, String> RedisOperations = valueOper.getOperations();
        RedisOperations.delete(key);
    }

}
