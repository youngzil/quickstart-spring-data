/**
 * 项目名称：quickstart-spring-boot-redis 
 * 文件名：WechatQRCodeController.java
 * 版本信息：
 * 日期：2017年11月28日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.redis.example2;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * WechatQRCodeController 
 *  
 * @author：youngzil@163.com
 * @2017年11月28日 下午1:03:32 
 * @since 1.0
 */
/**
 * 微信带参二维码
 * 
 * @author phil
 * @date 2017年7月24日
 *
 */
public class WechatQRCodeController {

    @Autowired
    private WechatQRCodeService wechatQRCodeService;

    @Autowired
    private RedisCacheManager redisCacheManager;

    public String test() {
        Object object = redisCacheManager.get("phil_token");
        if (object == null) {
            redisCacheManager.set("phil_token", HttpReqUtil.getAccessToken(WechatConfig.APP_ID, WechatConfig.APP_SECRET), 3600);
            object = redisCacheManager.get("phil_token");
        }
        wechatQRCodeService.createForeverStrTicket(object.toString(), "123");
        return null;
    }
}
