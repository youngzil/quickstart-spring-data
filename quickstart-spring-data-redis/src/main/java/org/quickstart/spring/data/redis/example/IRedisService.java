/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：IRedisService.java
 * 版本信息：
 * 日期：2017年11月27日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example;

import java.util.List;

/**
 * IRedisService
 * 
 * @author：youngzil@163.com
 * @2017年11月27日 上午11:45:35
 * @since 1.0
 */
public interface IRedisService {

    public boolean set(String key, String value);

    public String get(String key);

    public boolean expire(String key, long expire);

    public <T> boolean setList(String key, List<T> list);

    public <T> List<T> getList(String key, Class<T> clz);

    public long lpush(String key, Object obj);

    public long rpush(String key, Object obj);

    public String lpop(String key);

}
