package com.elasticsearch.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public void save(List<Restaurant> restaurants) {
        restaurantRepository.bulkInsert(restaurants);
    }
}
