/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：ExampleController.java
 * 版本信息：
 * 日期：2017年11月27日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ExampleController
 * 
 * @author：youngzil@163.com
 * @2017年11月27日 上午11:49:20
 * @since 1.0
 */
@RestController
public class ExampleController {

    @Autowired
    private IRedisService redisService;

    @RequestMapping("/redis/set")
    public String redisSet(@RequestParam("value") String value) {
        boolean isOk = redisService.set("name", value);
        return Boolean.toString(isOk);
    }

    @RequestMapping("/redis/get")
    public String redisGet() {
        String name = redisService.get("name");
        return name;
    }

}
