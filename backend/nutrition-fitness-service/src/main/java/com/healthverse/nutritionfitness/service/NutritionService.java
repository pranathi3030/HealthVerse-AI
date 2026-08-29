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
import com.healthverse.nutritionfitness.dto.DietGenerateRequest;
import com.healthverse.nutritionfitness.dto.DietGenerateResponse;

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

        String dietType = request.getDietType() != null ? request.getDietType() : "Standard";
        Integer duration = request.getDurationDays() != null ? request.getDurationDays() : 30;
        
        String mealPlan = "Standard AI Diet. Breakfast: Oatmeal. Lunch: Salad. Dinner: Salmon. (Fallback mock)";
        try {
            DietGenerateRequest aiRequest = new DietGenerateRequest();
            aiRequest.setGoal(profile.getGoals() != null ? profile.getGoals() : "Healthy Eating");
            aiRequest.setDiet_type(dietType);
            aiRequest.setAge(profile.getAge());
            aiRequest.setWeight(profile.getWeight());
            aiRequest.setHeight(profile.getHeight());

            String aiAgentUrl = System.getenv("AI_AGENT_URL");
            if (aiAgentUrl == null || aiAgentUrl.isBlank()) {
                aiAgentUrl = "http://localhost:8000";
            }
            ResponseEntity<DietGenerateResponse> aiResponse = restTemplate.postForEntity(
                aiAgentUrl + "/api/v1/agents/diet/generate",
                aiRequest,
                DietGenerateResponse.class
            );

            if (aiResponse.getStatusCode().is2xxSuccessful() && aiResponse.getBody() != null) {
                DietGenerateResponse res = aiResponse.getBody();
                mealPlan = String.join("\n", res.getMeal_suggestions()) + "\n\n" + res.getHydration_guidance();
            }
        } catch (Exception e) {
            mealPlan = "Error connecting to AI Agent Service: " + e.getMessage();
        }

        // Basic Macros
        int baseCalories = 2000;
        int proteinGrams = 150;
        int fatGrams = 60;
        int carbsGrams = 200;
        double waterLiters = 2.5;

        NutritionPlan plan = NutritionPlan.builder()
                .userId(userId)
                .planName("Personalized Nutrition Plan - " + dietType)
                .dailyCalories(baseCalories)
                .proteinGrams(proteinGrams)
                .carbsGrams(carbsGrams)
                .fatGrams(fatGrams)
                .waterLiters(Math.round(waterLiters * 10.0) / 10.0)
                .mealPlan(mealPlan)
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
