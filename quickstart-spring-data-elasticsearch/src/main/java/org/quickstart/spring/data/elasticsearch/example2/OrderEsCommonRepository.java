/**
 * 项目名称：quickstart-spring-data-elasticsearch 
 * 文件名：OrderEsCommonRepository.java
 * 版本信息：
 * 日期：2017年11月30日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.elasticsearch.example2;

/**
 * OrderEsCommonRepository http://blog.csdn.net/liuxigiant/article/details/52105024
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月30日 下午10:16:02
 * @since 1.0
 */
public interface OrderEsCommonRepository {
    /**
     * 创建索引
     * 
     * @return
     */
    public boolean createOrderIndex();
}
