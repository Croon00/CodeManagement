package com.kstec.codemangement.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClientTransport transport = new RestClientTransport(
                RestClient.builder(new HttpHost("localhost", 9200, "http")).build(),
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}
