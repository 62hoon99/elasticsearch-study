package com.elasticsearch.restaurant;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
}
