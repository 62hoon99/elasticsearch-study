package com.elasticsearch.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.elasticsearch.search.domain.NoriAnalyzerDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/elastic-search")
@Slf4j
public class SearchController {

    private final ElasticsearchClient esClient;

    @GetMapping("/nori-analyzer")
    public ResponseEntity<NoriAnalyzerDocument> getData(@RequestParam(value = "match") String matchQuery) throws IOException {

        SearchResponse<NoriAnalyzerDocument> response = esClient.search(s -> s
                        .index("holymoly")
                        .query(q -> q
                                .match(t -> t
                                        .field("description")
                                        .query(matchQuery)
                                )
                        ),
                NoriAnalyzerDocument.class
        );

//        NoriAnalyzerDocument noriAnalyzerDocument = new NoriAnalyzerDocument("holy");
//
//        IndexRequest.Builder<NoriAnalyzerDocument> indexReqBuilder = new IndexRequest.Builder<>();
//        indexReqBuilder.index("holymoly");
//        indexReqBuilder.id("1");
//        indexReqBuilder.document(noriAnalyzerDocument);
//
//        IndexResponse response = esClient.index(indexReqBuilder.build());
//
//        log.info("Indexed with version " + response.version());

        return ResponseEntity.ok(response.hits().hits().get(0).source());
    }
}
