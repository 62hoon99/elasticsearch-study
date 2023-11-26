package com.elasticsearch.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/restaurants/search")
    public ResponseEntity<Page<Restaurant>> saveRestaurants(@RequestParam(value = "q") String query,
                                                            Pageable pageable) {
        Page<Restaurant> response = restaurantService.search(query, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/restaurants/auto-completion")
    public ResponseEntity<Void> bulkInsertAutoCompletion() {
        restaurantService.bulkInsertAutoCompletion();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/restaurants/auto-completion")
    public ResponseEntity<List<String>> bulkInsertAutoCompletion(@RequestParam(value = "q") String query) {
        List<String> response = restaurantService.findAutoCompletions(query);

        return ResponseEntity.ok(response);
    }

}
