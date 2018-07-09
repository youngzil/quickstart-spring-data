/**
 * 项目名称：quickstart-spring-data-elasticsearch 
 * 文件名：CityRestControllerTest.java
 * 版本信息：
 * 日期：2017年12月2日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.spring.data.elasticsearch.example3.controller;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.quickstart.spring.data.elasticsearch.example3.Application;
import org.quickstart.spring.data.elasticsearch.example3.domain.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * CityRestControllerTest
 * 
 * @author：yangzl@asiainfo.com
 * @2017年12月2日 上午10:45:07
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@DirtiesContext
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CityRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateCity() throws Exception {

        City city = new City();
        city.setId(1L);
        city.setProvinceid(1L);
        city.setCityname("温岭");
        city.setDescription("温岭是个好城市");

        ResponseEntity<Long> entity = this.restTemplate.postForEntity("/api/city", city, Long.class);
        System.out.println("city=" + entity.getBody());

        City city2 = new City();
        city2.setId(2L);
        city2.setProvinceid(2L);
        city2.setCityname("温州");
        city2.setDescription("温州是个热城市");

        ResponseEntity<Long> entity2 = this.restTemplate.postForEntity("/api/city", city2, Long.class);
        System.out.println("city2=" + entity2.getBody());

    }

    @Test
    public void testSearchCity() throws Exception {

        StringBuffer param = new StringBuffer("pageNumber=0&pageSize=10&searchContent=");
        param.append("温岭");

        ResponseEntity<String> entity = this.restTemplate.getForEntity("/api/city/search?" + param, String.class);
        System.out.println(entity.getBody());

    }

}
