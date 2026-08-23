package com.healthverse.nutritionfitness.service;

import com.healthverse.nutritionfitness.dto.HealthProfileDto;
import com.healthverse.nutritionfitness.dto.NutritionPlanRequest;
import com.healthverse.nutritionfitness.dto.NutritionPlanResponse;
import com.healthverse.nutritionfitness.entity.NutritionPlan;
import com.healthverse.nutritionfitness.exception.BadRequestException;
import com.healthverse.nutritionfitness.exception.ResourceNotFoundException;
import com.healthverse.nutritionfitness.repository.NutritionPlanRepository;
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
public class NutritionService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final RestTemplate restTemplate;

    public NutritionService(NutritionPlanRepository nutritionPlanRepository, RestTemplate restTemplate) {
        this.nutritionPlanRepository = nutritionPlanRepository;
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

    public NutritionPlanResponse generateNutritionPlan(Long userId, NutritionPlanRequest request) {
        HealthProfileDto profile = getHealthProfile();
        
        if (profile == null) {
            throw new BadRequestException("Health profile not found for the user.");
        }

        // Dummy logic for calculating nutrition based on profile
        int baseCalories = 2000;
        if (profile.getWeight() != null && profile.getHeight() != null && profile.getAge() != null) {
            // Simplified BMR
            baseCalories = (int) (10 * profile.getWeight() + 6.25 * profile.getHeight() - 5 * profile.getAge());
            if ("Female".equalsIgnoreCase(profile.getGender())) {
                baseCalories -= 161;
            } else {
                baseCalories += 5;
            }
        }
        
        // Adjust for goal
        String goals = profile.getGoals() != null ? profile.getGoals().toLowerCase() : "";
        if (goals.contains("weight loss") || goals.contains("lose weight")) {
            baseCalories -= 500;
        } else if (goals.contains("muscle") || goals.contains("gain weight")) {
            baseCalories += 500;
        }

        // Macros
        int proteinGrams = (int) (profile.getWeight() != null ? profile.getWeight() * 1.6 : 150);
        int fatGrams = (baseCalories * 25) / 900;
        int carbsGrams = (baseCalories - (proteinGrams * 4) - (fatGrams * 9)) / 4;
        double waterLiters = (profile.getWeight() != null ? profile.getWeight() * 0.033 : 2.5);

        String dietType = request.getDietType() != null ? request.getDietType() : "Standard";
        Integer duration = request.getDurationDays() != null ? request.getDurationDays() : 30;

        NutritionPlan plan = NutritionPlan.builder()
                .userId(userId)
                .planName("Personalized Nutrition Plan - " + dietType)
                .dailyCalories(baseCalories)
                .proteinGrams(proteinGrams)
                .carbsGrams(carbsGrams)
                .fatGrams(fatGrams)
                .waterLiters(Math.round(waterLiters * 10.0) / 10.0)
                .mealPlan("Breakfast: Oatmeal. Lunch: Grilled Chicken Salad. Dinner: Salmon with Quinoa. (Dislcaimer: Not medical advice)")
                .dietType(dietType)
                .allergyRestrictions(profile.getAllergies() != null ? profile.getAllergies() : "None")
                .durationDays(duration)
                .active(true)
                .build();

        plan = nutritionPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public List<NutritionPlanResponse> getAllNutritionPlansForUser(Long userId) {
        return nutritionPlanRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public NutritionPlanResponse getNutritionPlanByIdForUser(Long id, Long userId) {
        NutritionPlan plan = nutritionPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition plan not found or you don't have access"));
        return mapToResponse(plan);
    }

    public NutritionPlanResponse updateNutritionPlan(Long id, Long userId, NutritionPlanRequest request) {
        NutritionPlan plan = nutritionPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition plan not found or you don't have access"));

        if (request.getDietType() != null) {
            plan.setDietType(request.getDietType());
        }
        if (request.getDurationDays() != null) {
            plan.setDurationDays(request.getDurationDays());
        }

        plan = nutritionPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public void deleteNutritionPlan(Long id, Long userId) {
        NutritionPlan plan = nutritionPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nutrition plan not found or you don't have access"));
        nutritionPlanRepository.delete(plan);
    }

    private NutritionPlanResponse mapToResponse(NutritionPlan plan) {
        return NutritionPlanResponse.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .planName(plan.getPlanName())
                .dailyCalories(plan.getDailyCalories())
                .proteinGrams(plan.getProteinGrams())
                .carbsGrams(plan.getCarbsGrams())
                .fatGrams(plan.getFatGrams())
                .waterLiters(plan.getWaterLiters())
                .mealPlan(plan.getMealPlan())
                .dietType(plan.getDietType())
                .allergyRestrictions(plan.getAllergyRestrictions())
                .durationDays(plan.getDurationDays())
                .active(plan.getActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
