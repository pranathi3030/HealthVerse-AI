package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.model.Restaurant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @GetMapping
    public List<Restaurant> getRestaurants() {
        return Arrays.asList(
            new Restaurant(1, "Paradise", "Hyderabad")
        );
    }
}
