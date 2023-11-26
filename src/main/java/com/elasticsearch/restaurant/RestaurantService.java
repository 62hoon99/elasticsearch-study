package com.elasticsearch.restaurant;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public void save(List<Restaurant> restaurants) {
        restaurantRepository.bulkInsert(restaurants);
    }

    public Page<Restaurant> search(String query, Pageable pageable) {
        SearchResponse<Restaurant> searchData = restaurantRepository.search(query, pageable.getPageNumber(), pageable.getPageSize());
        List<Restaurant> contents = searchData.hits().hits().stream().map(Hit::source).collect(Collectors.toList());

        return new PageImpl<>(contents, pageable, searchData.took());
    }

    /**
     * 자동완성 데이터를 넣기 위한 코드
     */
    public void bulkInsertAutoCompletion() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < 61; i++) {
            List<String> descriptions = restaurantRepository.searchAll(i, 100).hits().hits().stream()
                    .map(Hit::source).map(Restaurant::getDescription).collect(Collectors.toList());

            if (i % 10 == 0) {
                for (String description : descriptions) {
                    restaurantRepository.analyze(description).tokens().stream().map(AnalyzeToken::token).forEach(set::add);
                }
            }
        }

        List<AutoCompletion> autoCompletions = set.stream().map(AutoCompletion::new).collect(Collectors.toList());
        restaurantRepository.bulkInsertAutoCompletion(autoCompletions);
    }
}
