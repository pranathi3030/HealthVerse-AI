package com.healthverse.wellness.controller;

import com.healthverse.wellness.dto.WellnessActivityRequest;
import com.healthverse.wellness.dto.WellnessActivityResponse;
import com.healthverse.wellness.exception.BadRequestException;
import com.healthverse.wellness.service.WellnessActivityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/wellness/activities")
public class WellnessActivityController {

    private final WellnessActivityService wellnessActivityService;

    public WellnessActivityController(WellnessActivityService wellnessActivityService) {
        this.wellnessActivityService = wellnessActivityService;
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

    @PostMapping
    public ResponseEntity<WellnessActivityResponse> createWellnessActivity(
            Authentication authentication,
            @Valid @RequestBody WellnessActivityRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessActivityResponse response = wellnessActivityService.createWellnessActivity(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WellnessActivityResponse>> getAllWellnessActivities(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<WellnessActivityResponse> responses = wellnessActivityService.getAllWellnessActivitiesForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WellnessActivityResponse> getWellnessActivityById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessActivityResponse response = wellnessActivityService.getWellnessActivityByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WellnessActivityResponse> updateWellnessActivity(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody WellnessActivityRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessActivityResponse response = wellnessActivityService.updateWellnessActivity(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWellnessActivity(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        wellnessActivityService.deleteWellnessActivity(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<WellnessActivityResponse>> getWellnessActivitiesByDate(
            Authentication authentication,
            @PathVariable LocalDate date) {
        Long userId = extractUserIdFromAuth(authentication);
        List<WellnessActivityResponse> responses = wellnessActivityService.getWellnessActivitiesByDate(userId, date);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/completed")
    public ResponseEntity<List<WellnessActivityResponse>> getCompletedActivities(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<WellnessActivityResponse> responses = wellnessActivityService.getCompletedActivities(userId);
        return ResponseEntity.ok(responses);
    }
    
    @PatchMapping("/{id}/complete")
    public ResponseEntity<WellnessActivityResponse> markActivityCompleted(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessActivityResponse response = wellnessActivityService.markActivityCompleted(id, userId);
        return ResponseEntity.ok(response);
    }
}
