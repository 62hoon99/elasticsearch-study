package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchRepository<T> {

    private final ElasticsearchClient esClient;

    public CreateIndexResponse createIndex(CreateIndexRequest request) {
        try {
            return esClient.indices().create(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BulkResponse save(BulkRequest bulkRequest) {
        try {
            return esClient.bulk(bulkRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(String indexName, String id, Class<T> responseType) {
        try {
            return esClient.get(GetRequest.of(g -> g.index(indexName).id(id)), responseType).source();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
