package com.elasticsearch.movie;

import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
