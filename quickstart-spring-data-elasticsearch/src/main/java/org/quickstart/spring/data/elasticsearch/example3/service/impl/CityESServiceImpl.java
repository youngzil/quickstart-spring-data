package org.quickstart.spring.data.elasticsearch.example3.service.impl;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.quickstart.spring.data.elasticsearch.example3.domain.City;
import org.quickstart.spring.data.elasticsearch.example3.repository.CityRepository;
import org.quickstart.spring.data.elasticsearch.example3.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * 城市 ES 业务逻辑实现类
 *
 * Created by bysocket on 07/02/2017.
 */
@Service
public class CityESServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityESServiceImpl.class);

    @Autowired
    CityRepository cityRepository;

    // http://localhost:8888/save
    @Override
    public Long saveCity(City city) {

        City cityResult = cityRepository.save(city);
        return cityResult.getId();
    }

    // http://localhost:8888/delete?id=1525415333329
    public String delete(long id) {
        cityRepository.delete(id);
        return "success";
    }

    // http://localhost:8888/update?id=1525417362754&name=修改&description=修改
    public String update(City city) {
        cityRepository.save(city);
        return "success";
    }

    // http://localhost:8888/getOne?id=1525417362754
    public City getOne(long id) {
        City city = cityRepository.findOne(id);
        return city;
    }

    // 每页数量
    private Integer PAGESIZE = 10;

    // http://localhost:8888/getGoodsList?query=商品
    // http://localhost:8888/getGoodsList?query=商品&pageNumber=1
    // 根据关键字"商品"去查询列表，name或者description包含的都查询
    public List<City> getList(Integer pageNumber, String query) {
        if (pageNumber == null)
            pageNumber = 0;
        // es搜索默认第一页页码是0
        SearchQuery searchQuery = getEntitySearchQuery(pageNumber, PAGESIZE, query);
        Page<City> goodsPage = cityRepository.search(searchQuery);
        return goodsPage.getContent();
    }

    private SearchQuery getEntitySearchQuery(int pageNumber, int pageSize, String searchContent) {
        FunctionScoreQueryBuilder functionScoreQueryBuilder =
                QueryBuilders.functionScoreQuery().add(QueryBuilders.matchPhraseQuery("name", searchContent), ScoreFunctionBuilders.weightFactorFunction(100))
                        .add(QueryBuilders.matchPhraseQuery("description", searchContent), ScoreFunctionBuilders.weightFactorFunction(100))
                        // 设置权重分 求和模式
                        .scoreMode("sum")
                        // 设置权重分最低分
                        .setMinScore(10);

        // 设置分页
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder).build();
    }

    @Override
    public List<City> searchCity(Integer pageNumber, Integer pageSize, String searchContent) {
        // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);

        // Function Score Query
        FunctionScoreQueryBuilder functionScoreQueryBuilder =
                QueryBuilders.functionScoreQuery().add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("cityname", searchContent)), ScoreFunctionBuilders.weightFactorFunction(1000))
                        .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("description", searchContent)), ScoreFunctionBuilders.weightFactorFunction(100));

        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder).build();

        LOGGER.info("\n searchCity(): searchContent [" + searchContent + "] \n DSL  = \n " + searchQuery.getQuery().toString());

        Page<City> searchPageResults = cityRepository.search(searchQuery);
        return searchPageResults.getContent();
    }

}
