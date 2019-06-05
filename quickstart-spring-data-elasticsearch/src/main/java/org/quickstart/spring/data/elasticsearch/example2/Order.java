/**
 * 项目名称：quickstart-spring-data-elasticsearch 
 * 文件名：Order.java
 * 版本信息：
 * 日期：2017年11月30日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.elasticsearch.example2;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Order http://blog.csdn.net/liuxigiant/article/details/52105024
 * 
 * @author：youngzil@163.com
 * @2017年11月30日 下午10:14:33
 * @since 1.0
 */
// @Document注解指定了Order实体类对应的ES存储时的index（索引）名称和type（类型）名称
@Document(indexName = "test_es_order_index", type = "test_es_order_type")
public class Order {

    // @Id注解指定了主键
    @Id
    private Long id;
    private String userName;
    private String skuName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", userName='" + userName + '\'' + ", skuName='" + skuName + '\'' + '}';
    }
}
