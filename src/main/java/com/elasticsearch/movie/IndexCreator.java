package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class IndexCreator {

    private final ElasticsearchClient esClient;

    public CreateIndexResponse createIndex(CreateIndexRequest request) {
        try {
            return esClient.indices().create(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
