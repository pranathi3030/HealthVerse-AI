package com.klh.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/products")
    public String getProducts() {

        return """
                [
                  {
                    "id":1,
                    "name":"Laptop",
                    "price":65000
                  },
                  {
                    "id":2,
                    "name":"Mobile",
                    "price":25000
                  }
                ]
                """;
    }
}