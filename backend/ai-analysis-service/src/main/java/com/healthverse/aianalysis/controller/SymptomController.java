package com.healthverse.aianalysis.controller;

import com.healthverse.aianalysis.dto.HealthAnalyzeRequest;
import com.healthverse.aianalysis.dto.HealthAnalyzeResponse;
import com.healthverse.aianalysis.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/analysis/symptoms")
public class SymptomController {

    private final RestTemplate restTemplate;

    public SymptomController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private void checkAuth(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("User is not authenticated");
        }
    }

    @PostMapping
    public ResponseEntity<HealthAnalyzeResponse> analyzeSymptoms(
            Authentication authentication,
            @RequestBody HealthAnalyzeRequest request) {
        checkAuth(authentication);

        try {
            String aiAgentUrl = System.getenv("AI_AGENT_URL");
            if (aiAgentUrl == null || aiAgentUrl.isBlank()) {
                aiAgentUrl = "http://localhost:8000";
            }
            ResponseEntity<HealthAnalyzeResponse> response = restTemplate.postForEntity(
                    aiAgentUrl + "/api/v1/agents/health/analyze",
                    request,
                    HealthAnalyzeResponse.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            HealthAnalyzeResponse errResponse = new HealthAnalyzeResponse();
            errResponse.setSeverity("Unknown");
            errResponse.setInsights(java.util.List.of("Error connecting to AI service"));
            errResponse.setRecommendations(java.util.List.of("Please try again later."));
            errResponse.setSeekProfessionalCare(true);
            return ResponseEntity.status(500).body(errResponse);
        }
    }
}
