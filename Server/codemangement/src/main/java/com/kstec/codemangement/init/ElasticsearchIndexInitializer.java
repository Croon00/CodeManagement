//package com.kstec.codemangement.init;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
//import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
//import co.elastic.clients.elasticsearch.indices.IndexSettings;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//import java.io.IOException;
//
//@Component
//public class ElasticsearchIndexInitializer {
//
//    private final ElasticsearchClient elasticsearchClient;
//
//    public ElasticsearchIndexInitializer(ElasticsearchClient elasticsearchClient) {
//        this.elasticsearchClient = elasticsearchClient;
//    }
//
//    @PostConstruct
//    public void createIndexIfNotExists() {
//        try {
//            boolean indexExists = elasticsearchClient.indices().exists(e -> e.index("code_index")).value();
//
//            if (!indexExists) {
//                CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(
//                        CreateIndexRequest.of(c -> c
//                                .index("code_index")
//                                .settings(IndexSettings.of(s -> s
//                                        .numberOfShards("1")
//                                        .numberOfReplicas("1")
//                                ))
//                        )
//                );
//
//                if (createIndexResponse.acknowledged()) {
//                    System.out.println("✅ code_index가 생성되었습니다.");
//                } else {
//                    System.err.println("❌ code_index 생성 실패");
//                }
//            } else {
//                System.out.println("🔹 code_index가 이미 존재합니다.");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("🚨 Elasticsearch Index 생성 중 오류 발생", e);
//        }
//    }
//}
