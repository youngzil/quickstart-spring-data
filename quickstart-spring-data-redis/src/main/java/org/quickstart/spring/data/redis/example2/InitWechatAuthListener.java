/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：InitWechatAuthListener.java
 * 版本信息：
 * 日期：2017年11月28日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * InitWechatAuthListener 
 *  
 * @author：youngzil@163.com
 * @2017年11月28日 下午1:02:26 
 * @since 1.0
 */
/**
 * 比如微信的access_token有效期只有2小时，不可能即用即获取 初始化生成微信token
 * 
 * @author phil
 * @date 2017年7月9日
 *
 */
@Component
public class InitWechatAuthListener implements ApplicationListener<ContextRefreshedEvent> {

    // private static final Logger logger = Logger.getLogger(InitWechatAuthListener.class);

    @Autowired
    private RedisCacheManager redisCacheManager;

    private static boolean isStart = false; // 防止二次调用

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!isStart) {
            isStart = true;
            String token = HttpReqUtil.getAccessToken(WechatConfig.APP_ID, WechatConfig.APP_SECRET);
            redisCacheManager.set("phil_token", token, 3600); // 1小时
        }
    }
}
