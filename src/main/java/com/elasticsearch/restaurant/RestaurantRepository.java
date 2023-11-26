package com.elasticsearch.restaurant;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.movie.MovieDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantRepository {

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
                                .index("restaurant")
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
                            .index("restaurant")
                            .searchType(SearchType.DfsQueryThenFetch)
                            .query(Query.of(q -> q
                                    .multiMatch(v -> v
                                            .fields(List.of("city^2", "description", "category3^1.5", "name^1.3"))
                                            .query(query)
                                            .type(TextQueryType.BestFields)))
                            )
                            .from(pageNumber)
                            .size(pageSize)
                            .explain(true)
                    , Restaurant.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
