package com.hezebin.template.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import java.io.IOException;

@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Bean
    public RestClient restClient() {
        String[] uriParts = uris.replace("http://", "").split(":");
        return RestClient.builder(
                new HttpHost(uriParts[0], Integer.parseInt(uriParts[1]), "http"))
                .build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) throws IOException {
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper());

        ElasticsearchClient client = new ElasticsearchClient(transport);

        // 创建索引和映射
        try {
            client.indices().create(c -> c
                    .index("example")
                    .mappings(m -> m
                            .properties("id", p -> p.keyword(k -> k))
                            .properties("username", p -> p.text(t -> t.analyzer("standard")))
                            .properties("password", p -> p.text(t -> t))
                            .properties("email", p -> p.keyword(k -> k))
                            .properties("salt", p -> p.keyword(k -> k))));
        } catch (Exception e) {
            // 索引已存在的错误可以忽略
            if (!e.getMessage().contains("resource_already_exists_exception")) {
                throw e;
            }
        }

        return client;
    }
}