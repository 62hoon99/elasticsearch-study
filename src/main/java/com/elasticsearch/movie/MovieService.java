package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieElasticsearchRepository elasticsearchRepository;

    public CreateIndexResponse createMovieMappings() {
        return elasticsearchRepository.createIndex(MovieDocument.getCreateIndexRequest());
    }

    public BulkResponse bulkIndexingMultipleDocuments() {
        List<MovieDocument> movieDocuments = List.of(
                new MovieDocument("20173732", "살아남은 아이", "Last Child"),
                new MovieDocument("20184623", "바람난 이세돌2", "Windy Stone2"),
                new MovieDocument("20174244", "버블 패밀리", "Family in the Bubble"),
                new MovieDocument("20174245", "버블 패밀리2", "Family in the Bubble")
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
        return elasticsearchRepository.findById("movie_java", id);
    }

    public List<MovieDocument> findMovieNmByMovieCd(String movieCd, int page, int size) {
        return elasticsearchRepository.search(movieCd, page, size).hits().hits()
                .stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<MovieDocument> findMovieByMatchingQuery(String movieNm) {
        return elasticsearchRepository.search(movieNm).hits().hits()
                .stream().map(Hit::source).collect(Collectors.toList());
    }
}
