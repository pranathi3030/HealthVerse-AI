package com.healthverse.analytics.controller;

import com.healthverse.analytics.dto.HealthScoreResponse;
import com.healthverse.analytics.dto.TrendResponse;
import com.healthverse.analytics.dto.WeeklySummaryResponse;
import com.healthverse.analytics.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        if (authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to extract userId from authentication token");
    }

    @GetMapping("/summary")
    public ResponseEntity<WeeklySummaryResponse> getWeeklySummary(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        return ResponseEntity.ok(analyticsService.getWeeklySummary(userId));
    }

    @GetMapping("/health-score")
    public ResponseEntity<HealthScoreResponse> getHealthScore(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        return ResponseEntity.ok(analyticsService.getHealthScore(userId));
    }

    @GetMapping("/trends")
    public ResponseEntity<TrendResponse> getTrends(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        return ResponseEntity.ok(analyticsService.getTrends(userId));
    }
}
