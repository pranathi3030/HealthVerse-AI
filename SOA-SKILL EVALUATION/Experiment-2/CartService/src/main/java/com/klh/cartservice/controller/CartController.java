package com.klh.cartservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @GetMapping("/cart")
    public String getCart() {

        return """
                {
                  "cartId":101,
                  "items":3,
                  "totalAmount":90000
                }
                """;
    }
}