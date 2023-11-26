package com.elasticsearch.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/restaurants")
    public ResponseEntity<Void> saveRestaurants(@RequestPart(value = "file") MultipartFile file) {
        List<Restaurant> restaurants = RestaurantFileExtractor.extract(file);

        restaurantService.save(restaurants);

        return ResponseEntity.ok().build();
    }
}
