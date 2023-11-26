package com.elasticsearch.restaurant;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantRepository {

    private static final String RESTAURANT_INDEX_NAME = "restaurant";

    private final ElasticsearchClient esClient;

    public BulkResponse save(BulkRequest bulkRequest) {
        try {
            return esClient.bulk(bulkRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BulkResponse bulkInsert(List<Restaurant> restaurants) {
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();

            restaurants.forEach(restaurant -> {
                br.operations(op -> op
                        .index(idx -> idx
                                .index(RESTAURANT_INDEX_NAME)
                                .document(restaurant)
                        )
                );
            });

            return esClient.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResponse<Restaurant> search(String query, int pageNumber, int pageSize) {
        try {
            return esClient.search(s -> s
                            .index(RESTAURANT_INDEX_NAME)
                            .searchType(SearchType.DfsQueryThenFetch)
                            .query(Query.of(q -> q
                                    .multiMatch(v -> v
                                            .fields(List.of("city^2", "description", "category3^1.5", "name^1.3"))
                                            .query(query)
                                            .type(TextQueryType.BestFields)))
                            )
                            .from(pageNumber * pageSize)
                            .size(pageSize)
                            .explain(true)
                    , Restaurant.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SearchResponse<Restaurant> searchAll(int pageNumber, int pageSize) {
        try {
            return esClient.search(s -> s
                            .index(RESTAURANT_INDEX_NAME)
                            .searchType(SearchType.DfsQueryThenFetch)
                            .query(Query.of(q -> q
                                    .matchAll(m -> m))
                            )
                            .from(pageNumber * pageSize)
                            .size(pageSize)
                            .sort(sort -> sort
                                    .field(f -> f
                                            .field("name").order(SortOrder.Asc)))
                            .explain(true)
                    , Restaurant.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BulkResponse bulkInsertAutoCompletion(List<AutoCompletion> autoCompletions) {
        try {
            BulkRequest.Builder br = new BulkRequest.Builder();

            autoCompletions.forEach(autoCompletion -> {
                br.operations(op -> op
                        .index(idx -> idx
                                .index("auto_completion2")
                                .document(autoCompletion)
                        )
                );
            });

            return esClient.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnalyzeResponse analyze(String description) {
        try {
            AnalyzeRequest analyzeRequest = AnalyzeRequest.of(a -> a
                    .index(RESTAURANT_INDEX_NAME)
                    .field("description")
                    .text(description));

            return esClient.indices().analyze(analyzeRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
