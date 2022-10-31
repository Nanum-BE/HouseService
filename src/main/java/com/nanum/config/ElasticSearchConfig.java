package com.nanum.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.http.HttpHeaders;

@Configuration
@EnableElasticsearchRepositories(basePackages = "*")
public class ElasticSearchConfig extends AbstractElasticsearchConfig {

    @Override
    public RestHighLevelClient elasticsearchClient() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.elasticsearch+json;compatible-with=7");
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/vnd.elasticsearch+json;compatible-with=7");

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("3.35.80.242:9200")
                .withDefaultHeaders(httpHeaders)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
