package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final IndexCreator indexCreator;

    public CreateIndexResponse createMovieMappings() {
        return indexCreator.createIndex(MovieDocument.getCreateIndexRequest());
    }
}
