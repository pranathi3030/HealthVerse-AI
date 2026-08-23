package com.healthverse.wellness.controller;

import com.healthverse.wellness.dto.WellnessPlanRequest;
import com.healthverse.wellness.dto.WellnessPlanResponse;
import com.healthverse.wellness.exception.BadRequestException;
import com.healthverse.wellness.service.WellnessPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wellness/plans")
public class WellnessPlanController {

    private final WellnessPlanService wellnessPlanService;

    public WellnessPlanController(WellnessPlanService wellnessPlanService) {
        this.wellnessPlanService = wellnessPlanService;
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
    public ResponseEntity<WellnessPlanResponse> generateWellnessPlan(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessPlanResponse response = wellnessPlanService.generateWellnessPlan(userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping
    public ResponseEntity<WellnessPlanResponse> createWellnessPlan(
            Authentication authentication,
            @Valid @RequestBody WellnessPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessPlanResponse response = wellnessPlanService.createWellnessPlan(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WellnessPlanResponse>> getAllWellnessPlans(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<WellnessPlanResponse> responses = wellnessPlanService.getAllWellnessPlansForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WellnessPlanResponse> getWellnessPlanById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessPlanResponse response = wellnessPlanService.getWellnessPlanByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WellnessPlanResponse> updateWellnessPlan(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody WellnessPlanRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessPlanResponse response = wellnessPlanService.updateWellnessPlan(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWellnessPlan(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        wellnessPlanService.deleteWellnessPlan(id, userId);
        return ResponseEntity.noContent().build();
    }
}
