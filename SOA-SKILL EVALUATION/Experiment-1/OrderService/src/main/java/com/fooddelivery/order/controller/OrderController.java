package com.fooddelivery.order.controller;

import com.fooddelivery.order.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public Map<String, String> placeOrder(@RequestBody Order order) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order Placed Successfully");
        return response;
    }
}
