package com.healthverse.nutritionfitness.controller;

import com.healthverse.nutritionfitness.dto.NutritionPlanRequest;
import com.healthverse.nutritionfitness.dto.NutritionPlanResponse;
import com.healthverse.nutritionfitness.exception.BadRequestException;
import com.healthverse.nutritionfitness.service.NutritionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutrition/plans")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("User is not authenticated");
        }
        if (authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        throw new BadRequestException("Unable to extract userId from authentication token");
    }

    @PostMapping("/generate")
    public ResponseEntity<NutritionPlanResponse> generateNutritionPlan(
            Authentication authentication,
            @Valid @RequestBody(required = false) NutritionPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        if (request == null) {
            request = new NutritionPlanRequest();
        }
        NutritionPlanResponse response = nutritionService.generateNutritionPlan(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NutritionPlanResponse>> getAllNutritionPlans(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<NutritionPlanResponse> responses = nutritionService.getAllNutritionPlansForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionPlanResponse> getNutritionPlanById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        NutritionPlanResponse response = nutritionService.getNutritionPlanByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionPlanResponse> updateNutritionPlan(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody NutritionPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        NutritionPlanResponse response = nutritionService.updateNutritionPlan(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNutritionPlan(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        nutritionService.deleteNutritionPlan(id, userId);
        return ResponseEntity.noContent().build();
    }
}
