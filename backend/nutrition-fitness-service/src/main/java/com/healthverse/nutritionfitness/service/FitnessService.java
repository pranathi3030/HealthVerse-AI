package com.healthverse.nutritionfitness.service;

import com.healthverse.nutritionfitness.dto.FitnessPlanRequest;
import com.healthverse.nutritionfitness.dto.FitnessPlanResponse;
import com.healthverse.nutritionfitness.dto.HealthProfileDto;
import com.healthverse.nutritionfitness.entity.FitnessPlan;
import com.healthverse.nutritionfitness.exception.BadRequestException;
import com.healthverse.nutritionfitness.exception.ResourceNotFoundException;
import com.healthverse.nutritionfitness.repository.FitnessPlanRepository;
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
public class FitnessService {

    private final FitnessPlanRepository fitnessPlanRepository;
    private final RestTemplate restTemplate;

    public FitnessService(FitnessPlanRepository fitnessPlanRepository, RestTemplate restTemplate) {
        this.fitnessPlanRepository = fitnessPlanRepository;
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

    public FitnessPlanResponse generateFitnessPlan(Long userId, FitnessPlanRequest request) {
        HealthProfileDto profile = getHealthProfile();
        
        if (profile == null) {
            throw new BadRequestException("Health profile not found for the user.");
        }

        String goal = request.getGoal() != null ? request.getGoal() : (profile.getGoals() != null ? profile.getGoals() : "General Fitness");
        String fitnessLevel = request.getFitnessLevel() != null ? request.getFitnessLevel() : "Beginner";
        Integer weeklyFreq = request.getWeeklyFrequency() != null ? request.getWeeklyFrequency() : 3;
        Integer sessionDuration = request.getSessionDurationMinutes() != null ? request.getSessionDurationMinutes() : 45;
        Integer durationDays = request.getDurationDays() != null ? request.getDurationDays() : 30;

        int burnTarget = sessionDuration * 8; // dummy calc
        
        String workoutPlan = "Warm-up: 5 mins jogging. " +
                             "Main: 3 sets of 10 reps Squats, Pushups, Lunges based on " + fitnessLevel + " level. " +
                             "Cool-down: 5 mins stretching. (Disclaimer: Please consult a doctor before starting.)";

        FitnessPlan plan = FitnessPlan.builder()
                .userId(userId)
                .planName("Personalized " + fitnessLevel + " Plan")
                .goal(goal)
                .fitnessLevel(fitnessLevel)
                .weeklyFrequency(weeklyFreq)
                .sessionDurationMinutes(sessionDuration)
                .workoutPlan(workoutPlan)
                .calorieBurnTarget(burnTarget)
                .durationDays(durationDays)
                .active(true)
                .build();

        plan = fitnessPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public List<FitnessPlanResponse> getAllFitnessPlansForUser(Long userId) {
        return fitnessPlanRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FitnessPlanResponse getFitnessPlanByIdForUser(Long id, Long userId) {
        FitnessPlan plan = fitnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness plan not found or you don't have access"));
        return mapToResponse(plan);
    }

    public FitnessPlanResponse updateFitnessPlan(Long id, Long userId, FitnessPlanRequest request) {
        FitnessPlan plan = fitnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness plan not found or you don't have access"));

        if (request.getGoal() != null) plan.setGoal(request.getGoal());
        if (request.getFitnessLevel() != null) plan.setFitnessLevel(request.getFitnessLevel());
        if (request.getWeeklyFrequency() != null) plan.setWeeklyFrequency(request.getWeeklyFrequency());
        if (request.getSessionDurationMinutes() != null) plan.setSessionDurationMinutes(request.getSessionDurationMinutes());
        if (request.getDurationDays() != null) plan.setDurationDays(request.getDurationDays());

        plan = fitnessPlanRepository.save(plan);
        return mapToResponse(plan);
    }

    public void deleteFitnessPlan(Long id, Long userId) {
        FitnessPlan plan = fitnessPlanRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness plan not found or you don't have access"));
        fitnessPlanRepository.delete(plan);
    }

    private FitnessPlanResponse mapToResponse(FitnessPlan plan) {
        return FitnessPlanResponse.builder()
                .id(plan.getId())
                .userId(plan.getUserId())
                .planName(plan.getPlanName())
                .goal(plan.getGoal())
                .fitnessLevel(plan.getFitnessLevel())
                .weeklyFrequency(plan.getWeeklyFrequency())
                .sessionDurationMinutes(plan.getSessionDurationMinutes())
                .workoutPlan(plan.getWorkoutPlan())
                .calorieBurnTarget(plan.getCalorieBurnTarget())
                .durationDays(plan.getDurationDays())
                .active(plan.getActive())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}
