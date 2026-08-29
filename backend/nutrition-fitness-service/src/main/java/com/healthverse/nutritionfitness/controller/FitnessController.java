package com.healthverse.nutritionfitness.controller;

import com.healthverse.nutritionfitness.dto.FitnessPlanRequest;
import com.healthverse.nutritionfitness.dto.FitnessPlanResponse;
import com.healthverse.nutritionfitness.exception.BadRequestException;
import com.healthverse.nutritionfitness.service.FitnessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/plans")
public class FitnessController {

    private final FitnessService fitnessService;

    public FitnessController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
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
    public ResponseEntity<FitnessPlanResponse> generateFitnessPlan(
            Authentication authentication,
            @Valid @RequestBody(required = false) FitnessPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        if (request == null) {
            request = new FitnessPlanRequest();
        }
        FitnessPlanResponse response = fitnessService.generateFitnessPlan(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FitnessPlanResponse>> getAllFitnessPlans(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<FitnessPlanResponse> responses = fitnessService.getAllFitnessPlansForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FitnessPlanResponse> getFitnessPlanById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        FitnessPlanResponse response = fitnessService.getFitnessPlanByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FitnessPlanResponse> updateFitnessPlan(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody FitnessPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        FitnessPlanResponse response = fitnessService.updateFitnessPlan(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFitnessPlan(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        fitnessService.deleteFitnessPlan(id, userId);
        return ResponseEntity.noContent().build();
    }
}
