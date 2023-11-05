package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ElasticsearchRepository<MovieDocument> elasticsearchRepository;

    public CreateIndexResponse createMovieMappings() {
        return elasticsearchRepository.createIndex(MovieDocument.getCreateIndexRequest());
    }

    public BulkResponse bulkIndexingMultipleDocuments() {
        List<MovieDocument> movieDocuments = List.of(
                new MovieDocument("20173732", "살아남은 아이", "Last Child"),
                new MovieDocument("20184623", "바람난 이세돌2", "Windy Stone2"),
                new MovieDocument("20174244", "버블 패밀리", "Family in the Bubble")
        );

        BulkRequest.Builder br = new BulkRequest.Builder();

        movieDocuments.forEach(movie -> {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("movie_java")
                            .id(movie.getMovieCd())
                            .document(movie)
                    )
            );
        });

        return elasticsearchRepository.save(br.build());
    }

    public MovieDocument findMovieDocument(String id) {
        return elasticsearchRepository.findById("movie_java", id, MovieDocument.class);
    }
}
