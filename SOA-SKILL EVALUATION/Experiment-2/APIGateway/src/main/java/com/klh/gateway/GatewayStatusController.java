package com.klh.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayStatusController {

    @GetMapping("/")
    public String status() {
        return "API Gateway is running. Use /products or /cart.";
    }
}
