package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieElasticsearchRepository {

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

    public MovieDocument findById(String indexName, String id) {
        try {
            return esClient.get(GetRequest.of(g -> g.index(indexName).id(id)), MovieDocument.class).source();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResponse<MovieDocument> search(String movieCd, int page, int size) {
        try {
            return esClient.search(s -> s
                            .index("movie_java")
                            .searchType(SearchType.DfsQueryThenFetch)
                            .query(Query.of(q -> q
                                    .term(t -> t
                                            .field("movieCd")
                                            .value(movieCd)
                                    ))
                            )
                            .from(page)
                            .size(size)
                            .explain(true)
                    , MovieDocument.class); // <- index가 여러개일 때 어떡해야 하지

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
