package org.quickstart.spring.data.elasticsearch.example;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;
import org.quickstart.spring.data.elasticsearch.example.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ElasticSearchManualTest {
    private List<Person> listOfPersons = new ArrayList<>();
    private Client client = null;

    @Before
    public void setUp() throws IOException {
        Person person1 = new Person(10, "John Doe", new Date());
        Person person2 = new Person(25, "Janette Doe", new Date());
        listOfPersons.add(person1);
        listOfPersons.add(person2);
        final Path tmpDir = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "elasticsearch_data");
        Node node = nodeBuilder().settings(Settings.settingsBuilder().put("http.enabled", false).put("path.home", "/usr/local/Cellar/elasticsearch/2.3.2"))
                // .settings(Settings.settingsBuilder().put("http.enabled", false).put("path.data", tmpDir.toAbsolutePath().toString()).put("path.home", "/usr/local/Cellar/elasticsearch/2.3.2"))
                // .clusterName("elasticsearch")
                .client(true).node();

        /* NodeBuilder.nodeBuilder()
        .settings(Settings.builder()
            .put("path.home", "/path/to/elasticsearch/home/dir")
        .node();*/

        client = node.client();
    }

    @Test
    public void givenJsonString_whenJavaObject_thenIndexDocument() {
        String jsonObject = "{\"age\":20,\"dateOfBirth\":1471466076564,\"fullName\":\"John Doe\"}";
        IndexResponse response = client.prepareIndex("people", "Doe").setSource(jsonObject).get();
        String index = response.getIndex();
        String type = response.getType();
        assertTrue(response.isCreated());
        assertEquals(index, "people");
        assertEquals(type, "Doe");
    }

    @Test
    public void givenDocumentId_whenJavaObject_thenDeleteDocument() {
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471455886564,\"fullName\":\"Johan Doe\"}";
        IndexResponse response = client.prepareIndex("people", "Doe").setSource(jsonObject).get();
        String id = response.getId();
        DeleteResponse deleteResponse = client.prepareDelete("people", "Doe", id).get();
        assertTrue(deleteResponse.isFound());
    }

    @Test
    public void givenSearchRequest_whenMatchAll_thenReturnAllResults() {
        SearchResponse response = client.prepareSearch().execute().actionGet();
        SearchHit[] searchHits = response.getHits().getHits();
        List<Person> results = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Person person = JSON.parseObject(sourceAsString, Person.class);
            results.add(person);
        }
    }

    @Test
    public void givenSearchParameters_thenReturnResults() {
        SearchResponse response = client.prepareSearch().setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setPostFilter(QueryBuilders.rangeQuery("age").from(5).to(15)).setFrom(0).setSize(60)
                .setExplain(true).execute().actionGet();

        SearchResponse response2 = client.prepareSearch().setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setPostFilter(QueryBuilders.simpleQueryStringQuery("+John -Doe OR Janette"))
                .setFrom(0).setSize(60).setExplain(true).execute().actionGet();

        SearchResponse response3 = client.prepareSearch().setTypes().setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setPostFilter(QueryBuilders.matchQuery("John", "Name*")).setFrom(0).setSize(60)
                .setExplain(true).execute().actionGet();
        response2.getHits();
        response3.getHits();
        List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
        final List<Person> results = new ArrayList<>();
        searchHits.forEach(hit -> results.add(JSON.parseObject(hit.getSourceAsString(), Person.class)));
    }

    @Test
    public void givenContentBuilder_whenHelpers_thanIndexJson() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("fullName", "Test").field("salary", "11500").field("age", "10").endObject();
        IndexResponse response = client.prepareIndex("people", "Doe").setSource(builder).get();
        assertTrue(response.isCreated());
    }
}
