package com.healthverse.wellness.service;

import com.healthverse.wellness.entity.WellnessActivity;
import com.healthverse.wellness.repository.WellnessActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictiveWellnessInsightService {

    private final WellnessActivityRepository wellnessActivityRepository;
    private final RestTemplate restTemplate;

    public Object getPredictiveInsights(Long userId) {
        List<WellnessActivity> history = wellnessActivityRepository.findByUserIdOrderByActivityDateDesc(userId);
        
        List<Map<String, Object>> mappedHistory = history.stream().limit(10).map(activity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", activity.getActivityDate() != null ? activity.getActivityDate().toString() : "unknown");
            map.put("durationMinutes", activity.getDurationMinutes());
            map.put("completed", activity.getCompleted());
            map.put("moodBefore", activity.getMoodBefore());
            map.put("moodAfter", activity.getMoodAfter());
            map.put("notes", activity.getNotes());
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> request = new HashMap<>();
        request.put("history", mappedHistory);

        String aiAgentUrl = System.getenv("AI_AGENT_URL");
        if (aiAgentUrl == null || aiAgentUrl.isBlank()) {
            aiAgentUrl = "http://localhost:8000";
        }

        try {
            ResponseEntity<Object> aiResponse = restTemplate.postForEntity(
                    aiAgentUrl + "/api/v1/agents/wellness/predict",
                    request,
                    Object.class
            );
            return aiResponse.getBody();
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("area", "Wellness API Error");
            fallback.put("trend", "Unknown");
            fallback.put("reason", "Could not reach AI service: " + e.getMessage());
            fallback.put("recommendation", "Ensure AI service is running on port 8000");
            fallback.put("confidence", "Low");
            fallback.put("contributing_factors", List.of());
            fallback.put("evidence", List.of());
            return fallback;
        }
    }
}
