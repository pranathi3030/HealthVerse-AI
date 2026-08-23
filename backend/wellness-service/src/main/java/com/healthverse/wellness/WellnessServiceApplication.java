package com.healthverse.wellness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WellnessServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WellnessServiceApplication.class, args);
    }
}
