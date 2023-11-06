package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/mappings")
    public ResponseEntity<CreateIndexResponse> createMovieMappings() {
        CreateIndexResponse response = movieService.createMovieMappings();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-indexing")
    public ResponseEntity<BulkResponse> bulkIndexingMultipleDocuments() {
        BulkResponse response = movieService.bulkIndexingMultipleDocuments();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDocument> findMovieDocument(@PathVariable(value = "id") String id) {
        MovieDocument movieDocument = movieService.findMovieDocument(id);

        return ResponseEntity.ok(movieDocument);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDocument>> findMovieNmByMovieCd(@RequestParam String movieCd,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size) {
        List<MovieDocument> movieCds = movieService.findMovieNmByMovieCd(movieCd, page, size);

        return ResponseEntity.ok(movieCds);
    }

    @GetMapping("/match-query")
    public ResponseEntity<List<MovieDocument>> findMovieByMatchingQuery(@RequestParam String movieNm) {
        List<MovieDocument> movieDocuments = movieService.findMovieByMatchingQuery(movieNm);

        return ResponseEntity.ok(movieDocuments);
    }
}
