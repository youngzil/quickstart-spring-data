package org.quickstart.spring.data.elasticsearch.example.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.quickstart.spring.data.elasticsearch.example.repository")
@ComponentScan(basePackages = {"org.quickstart.spring.data.elasticsearch.example.service"})
public class Config {

    @Value("${elasticsearch.home:/usr/local/Cellar/elasticsearch/2.3.2}")
    private String elasticsearchHome;

    private static Logger logger = LoggerFactory.getLogger(Config.class);

    @Bean
    public Client client() {
        try {
            final Path tmpDir = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "elasticsearch_data");
            logger.debug(tmpDir.toAbsolutePath().toString());

            // @formatter:off

            final Settings.Builder elasticsearchSettings =
                    Settings.settingsBuilder().put("http.enabled", "false").put("path.data", tmpDir.toAbsolutePath().toString()).put("path.home", elasticsearchHome);

            return new NodeBuilder().local(true).settings(elasticsearchSettings).node().client();

            // return NodeBuilder.nodeBuilder().data(true).settings(
            // Settings.builder()
            // .put(ClusterName.SETTING, "test")
            // .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 1)
            // .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 0)
            // .put(EsExecutors.PROCESSORS, 1)
            // .put("index.store.type", "memory")
            // ).build().client();

            /*return NodeBuilder.nodeBuilder()
            .settings(Settings.settingsBuilder().put("http.enabled", false))
            .client(true)
            .node().client();*/

            // @formatter:on
        } catch (final IOException ioex) {
            logger.error("Cannot create temp dir", ioex);
            throw new RuntimeException();
        }
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}
