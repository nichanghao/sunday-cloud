package net.sunday.cloud.base.test.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.io.IOException;

/**
 * test es client ssl connection
 */

@Slf4j
@SpringBootTest(classes = {
        // 如果没有开启ssl/tls,不需要此配置类
        SslAutoConfiguration.class,
        ElasticsearchRestClientAutoConfiguration.class,
        ElasticsearchClientAutoConfiguration.class})
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'local'}", loadContext = true)
public class ElasticSearchClientTest {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Test
    public void testElasticSearch() throws IOException {

        IndicesResponse indices = elasticsearchClient.cat().indices();

        log.info("indices: {}", indices);

    }
}
