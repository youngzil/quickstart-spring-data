/**
 * 项目名称：quickstart-spring-data-elasticsearch 
 * 文件名：OrderRepositoryImpl.java
 * 版本信息：
 * 日期：2017年11月30日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.elasticsearch.example2;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * OrderRepositoryImpl 
 *  http://blog.csdn.net/liuxigiant/article/details/52105024
 * @author：youngzil@163.com
 * @2017年11月30日 下午10:17:28 
 * @since 1.0
 */
/**
 * 自定义Repository实现类
 *
 * 接口的实现类名字后缀必须为impl才能在扫描包时被找到（可参考spring data elasticsearch自定义repository章节）
 *
 */
public class OrderRepositoryImpl implements OrderEsCommonRepository {

    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 创建索引
     * 
     * @return
     */
    public boolean createOrderIndex() {
        return elasticsearchTemplate.createIndex(Order.class);
    }

    // 自定义实现可以使用ElasticsearchTemplate做复杂的查询，例如：分组、聚合等，
    // 增加了spring data elasticsearch的灵活度，使用方法名定义和Query注解实现困难的查询操作，可借助ElasticsearchTemplate实现自定义的查询

    public void setElasticsearchTemplate(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

}
