/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：JedisApiTest.java
 * 版本信息：
 * 日期：2017年11月28日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JedisApiTest
 * 
 * @author：youngzil@163.com
 * @2017年11月28日 下午12:00:18
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
@DirtiesContext
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// 三种执行顺序可供选择：默认（MethodSorters.DEFAULT），按方法名（MethodSorters.NAME_ASCENDING）和JVM（MethodSorters.JVM）
public class SpringRedisJedisApiTest {
    // 这里别写泛型，因为StringRedisTemplate的源码是默 extends RedisTemplate<String, User> 你写泛型了就无法正确按照类型匹配了
    @Autowired
    private RedisTemplate stringRedisTemplate;
    // 这里为什么能这样注入，是利用了spring 的Editor特性，具体见下面博客
    // http://www.cnblogs.com/chanedi/p/4135303.html
    // 当然spring data redis 的官网也推荐了这个用法，注意这个特性是在 spring 3.2.8以上才有的，以后写demo要注意官方文档的requrement的提示
    // http://docs.spring.io/spring-data/redis/docs/1.4.0.RELEASE/reference/html/#redis:template
    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> listOps;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOps;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, Object> hashOps;

    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> setOps;

    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, String> ZSetOps;

    // redis命令速查表 http://doc.redisfans.com/

    // List的操作
    public void testListOps() {
        // List 其实是一个双向链表，从下面的代码就能看出来可以从两边进行push/pop操作
        stringRedisTemplate.delete("listTest"); // 删除这个List
        // 添加操作
        listOps.rightPush("listTest", "1");
        listOps.rightPush("listTest", "2");
        listOps.leftPush("listTest", "3");
        listOps.leftPush("listTest", "4");

        // 查看listTest这个List的值
        System.out.println(listOps.range("listTest", 0, -1)); // [4, 3, 1, 2]

        // 取左边第一个值
        System.out.println(listOps.leftPop("listTest")); // 4

        // 查看listTest这个List的值
        System.out.println(listOps.range("listTest", 0, -1)); // [3, 1, 2]

        // 修改操作 将下标为1的值修改为 "set_1"，(下标从0开始)
        listOps.set("listTest", 1, "set_1");
        System.out.println(listOps.range("listTest", 0, -1)); // [3, set_1, 2]

        // 删除操作 将下标为1，值为 set_1的元素删除
        listOps.remove("listTest", 1, "set_1");
        System.out.println(listOps.range("listTest", 0, -1)); // [3, 2]
    }

    // set的操作
    public void testSetOps() {
        // 添加
        setOps.add("setTest1", "1", "2", "3", "4");
        setOps.add("setTest2", "a", "b", "1", "2");

        // 查询 set集合是无序的
        System.out.println(setOps.members("setTest1"));// [2, 1, 3, 4]

        // 判断是否有这个值
        System.out.println(setOps.isMember("setTest1", "4"));// true

        // 删除 可以删除多个值
        setOps.remove("setTest1", "1", "4");
        System.out.println(setOps.members("setTest1"));// [3, 2]

        // 交集
        System.out.println(setOps.intersect("setTest1", "setTest2"));// [2]

        // 并集
        System.out.println(setOps.union("setTest1", "setTest2"));// [d, 2, c, 1, a, 3, b]

        // 差集
        System.out.println(setOps.difference("setTest1", "setTest2"));// [3]
    }

    // ZSetOps操作
    public void testZSetOps() {
        // 添加
        ZSetOps.add("ZSetTest", "google.com", 10);
        ZSetOps.add("ZSetTest", "baidu.com", 8);
        ZSetOps.add("ZSetTest", "soso.com", 5);
        ZSetOps.add("ZSetTest", "360.com", 2);

        // 默认按照分数的降序排列
        System.out.println(ZSetOps.range("ZSetTest", 0, -1));
        // [360.com, soso.com, baidu.com, google.com]

        // 按照分数范围查询
        System.out.println(ZSetOps.rangeByScore("ZSetTest", 5, 10));
        // [soso.com, baidu.com, google.com]

        // 查看排名
        System.out.println(ZSetOps.rank("ZSetTest", "baidu.com"));// 2 说明第一名是0

        // 按分数从大到小排列
        System.out.println(ZSetOps.reverseRange("ZSetTest", 0, -1));
        // [google.com, baidu.com, soso.com, 360.com]

        // 看这个对象的 socre
        System.out.println(ZSetOps.score("ZSetTest", "google.com")); // 10.0

        // 删除,按照对象
        ZSetOps.remove("ZSetTest", "360.com");
        // 删除，根据下标
        ZSetOps.removeRange("ZSetTest", 0, 1);
        // 删除，根据score
        ZSetOps.removeRangeByScore("ZSetTest", 2, 5);
    }

    // hash表操作 和Map差不多
    public void testHashOps() {
        // 添加
        hashOps.put("hashTest", "key1", "aa");
        Map<String, String> map = new HashMap<String, String>();
        map.put("key2", "bb");
        map.put("key3", "cc");
        map.put("key4", "dd");
        hashOps.putAll("hashTest", map);

        // 查询所有的key
        System.out.println(hashOps.keys("hashTest")); // [key3, key4, key2, key1]

        // 查询 所有的value
        System.out.println(hashOps.values("hashTest"));// [aa, bb, cc, dd]

        // 删除 key
        hashOps.delete("hashTest", "key1");
        System.out.println(hashOps.keys("hashTest")); // [key2, key4, key3]
    }

    // String操作
    public void testValueOps() {
        valueOps.set("valueTest", "1");
        System.out.println(valueOps.get("valueTest"));
    }
}
