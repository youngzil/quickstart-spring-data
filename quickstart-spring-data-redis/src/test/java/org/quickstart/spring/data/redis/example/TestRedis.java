/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：TestRedis.java
 * 版本信息：
 * 日期：2017年11月27日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runners.MethodSorters;

/**
 * TestRedis
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月27日 上午10:47:26
 * @since 1.0
 */
// @SpringBootConfiguration()
// @RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@DirtiesContext
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// 三种执行顺序可供选择：默认（MethodSorters.DEFAULT），按方法名（MethodSorters.NAME_ASCENDING）和JVM（MethodSorters.JVM）
public class TestRedis {

    @Autowired
    private IRedisService redisService;

    @Test
    // @Ignore
    public void redis1Set() {
        boolean isOk = redisService.set("name", "yangzl23456");
        System.out.println(isOk);
        Assert.assertTrue(isOk);
    }

    @Test
    // @Ignore
    public void redis2Get() {
        String name = redisService.get("name");
        Assert.assertEquals("yangzl23456", name);
    }

}
