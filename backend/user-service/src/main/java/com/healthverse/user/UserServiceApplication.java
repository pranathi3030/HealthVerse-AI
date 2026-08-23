package com.healthverse.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * HealthVerse AI - User & Health Profile Service
 * Manages user accounts, health profiles, BMI metrics, and lifestyle parameters.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
