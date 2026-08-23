package com.healthverse.wellness.service;

import com.healthverse.wellness.dto.HealthProfileDto;
import com.healthverse.wellness.dto.WellnessPlanRequest;
import com.healthverse.wellness.dto.WellnessPlanResponse;
import com.healthverse.wellness.entity.WellnessPlan;
import com.healthverse.wellness.exception.BadRequestException;
import com.healthverse.wellness.exception.ResourceNotFoundException;
import com.healthverse.wellness.repository.WellnessPlanRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WellnessPlanService {

    private final WellnessPlanRepository wellnessPlanRepository;
    private final RestTemplate restTemplate;

    public WellnessPlanService(WellnessPlanRepository wellnessPlanRepository, RestTemplate restTemplate) {
        this.wellnessPlanRepository = wellnessPlanRepository;
        this.restTemplate = restTemplate;
    }

    private HealthProfileDto getHealthProfile() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BadRequestException("Request attributes not found");
        }
        
        String authHeader = attributes.getRequest().getHeader("Authorization");
        if (authHeader == null) {
            throw new BadRequestException("Authorization header missing");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<HealthProfileDto> response = restTemplate.exchange(
                    "http://user-service/health/profile",
                    HttpMethod.GET,
                    entity,
                    HealthProfileDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch health profile: " + e.getMessage());
        }
    }

    public WellnessPlanResponse generateWellnessPlan(Long userId) {
        HealthProfileDto profile = getHealthProfile();
        
        if (profile == null) {
            throw new BadRequestException("Health profile not found for the user.");
        }

        String lifestyle = profile.getLifestyle() != null ? profile.getLifestyle().toLowerCase() : "";
        String goals = profile.getGoals() != null ? profile.getGoals().toLowerCase() : "";
        
        String title = "General Wellness Plan";
        String description = "A balanced plan for general well-being. (Disclaimer: Not medical advice)";
        String category = "General";
        int durationDays = 30;
        int dailyMinutes = 15;
        String priority = "Medium";

        if (lifestyle.contains("stress") || lifestyle.contains("sedentary")) {
            title = "Stress Management & Active Life Plan";
            description = "Focuses on relaxation, breathing, and walking to combat stress and a sedentary lifestyle.";
            category = "Stress Management";
            dailyMinutes = 20;
            priority = "High";
        } else if (goals.contains("sleep")) {
            title = "Better Sleep Plan";
            description = "Evening routines, meditation, and reduced screen activity for better sleep.";
            category = "Sleep";
            dailyMinutes = 30;
            priority = "High";
        } else if (goals.contains("fitness") || goals.contains("weight") || goals.contains("muscle")) {
            title = "Active Recovery & Mindfulness";
            description = "Stretching, recovery, and mindfulness to support your fitness goals.";
            category = "Mindfulness";
            dailyMinutes = 15;
            priority = "Medium";
        }

        WellnessPlan plan = WellnessPlan.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .category(category)
                .goal(goals.isEmpty() ? "General Well-being" : profile.getGoals())
                .durationDays(durationDays)
                .dailyMinutes(dailyMinutes)
                .priority(priority)
                .active(true)
                .build();

        plan = wellnessPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public WellnessPlanResponse createWellnessPlan(Long userId, WellnessPlanRequest request) {
        WellnessPlan plan = WellnessPlan.builder()
                .userId(userId)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .goal(request.getGoal())
                .durationDays(request.getDurationDays())
                .dailyMinutes(request.getDailyMinutes())
                .priority(request.getPriority())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        plan = wellnessPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public List<WellnessPlanResponse> getAllWellnessPlansForUser(Long userId) {
        return wellnessPlanRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WellnessPlanResponse getWellnessPlanByIdForUser(Long id, Long userId) {
        WellnessPlan plan = wellnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness plan not found or you don't have access"));
        return mapToResponse(plan);
    }

    public WellnessPlanResponse updateWellnessPlan(Long id, Long userId, WellnessPlanRequest request) {
        WellnessPlan plan = wellnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness plan not found or you don't have access"));

        if (request.getTitle() != null) plan.setTitle(request.getTitle());
        if (request.getDescription() != null) plan.setDescription(request.getDescription());
        if (request.getCategory() != null) plan.setCategory(request.getCategory());
        if (request.getGoal() != null) plan.setGoal(request.getGoal());
        if (request.getDurationDays() != null) plan.setDurationDays(request.getDurationDays());
        if (request.getDailyMinutes() != null) plan.setDailyMinutes(request.getDailyMinutes());
        if (request.getPriority() != null) plan.setPriority(request.getPriority());
        if (request.getActive() != null) plan.setActive(request.getActive());

        plan = wellnessPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public void deleteWellnessPlan(Long id, Long userId) {
        WellnessPlan plan = wellnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness plan not found or you don't have access"));
        wellnessPlanRepository.delete(plan);
    }

    private WellnessPlanResponse mapToResponse(WellnessPlan plan) {
        return WellnessPlanResponse.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .title(plan.getTitle())
                .description(plan.getDescription())
                .category(plan.getCategory())
                .goal(plan.getGoal())
                .durationDays(plan.getDurationDays())
                .dailyMinutes(plan.getDailyMinutes())
                .priority(plan.getPriority())
                .active(plan.getActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
