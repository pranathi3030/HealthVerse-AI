package com.healthverse.wellness.controller;

import com.healthverse.wellness.service.PredictiveWellnessInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wellness")
@RequiredArgsConstructor
public class PredictiveWellnessInsightController {

    private final PredictiveWellnessInsightService insightService;

    @GetMapping("/insights")
    public ResponseEntity<Object> getInsights(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(insightService.getPredictiveInsights(userId));
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        return null;
    }
}
