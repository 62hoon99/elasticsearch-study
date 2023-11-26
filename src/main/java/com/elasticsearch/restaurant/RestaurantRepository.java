package com.elasticsearch.restaurant;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
                                .index("restaurants")
                                .document(restaurant)
                        )
                );
            });

            return esClient.bulk(br.build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
