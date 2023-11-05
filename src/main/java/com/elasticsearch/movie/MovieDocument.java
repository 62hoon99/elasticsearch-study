package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch._types.mapping.IndexOptions;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;

import java.util.HashMap;
import java.util.Map;

public class MovieDocument {
    private String movieCd;
    private String movieNm;
    private String movieNmEn;

    public static CreateIndexRequest getCreateIndexRequest() {
        Map<String, Property> propertyMap = new HashMap<>();
        propertyMap.put("movieCd", Property.of(p -> p.keyword(k -> k.store(true).indexOptions(IndexOptions.Docs))));
        propertyMap.put("movieNm", Property.of(p -> p.text(t -> t.store(true).indexOptions(IndexOptions.Docs))));
        propertyMap.put("movieNmEn", Property.of(p -> p.text(t -> t.store(true).indexOptions(IndexOptions.Docs))));

        return CreateIndexRequest.of(c -> c
                .index("movie_java")
                .mappings(m -> m
                        .properties(propertyMap))
                .settings(s -> s
                        .numberOfShards("3")
                        .numberOfReplicas("1"))
        );
    }
}
