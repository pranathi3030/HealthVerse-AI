package com.healthverse.wellness.controller;

import com.healthverse.wellness.dto.WellnessSummaryResponse;
import com.healthverse.wellness.exception.BadRequestException;
import com.healthverse.wellness.service.WellnessActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wellness/summary")
public class WellnessSummaryController {

    private final WellnessActivityService wellnessActivityService;

    public WellnessSummaryController(WellnessActivityService wellnessActivityService) {
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

    @GetMapping
    public ResponseEntity<WellnessSummaryResponse> getWellnessSummary(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        WellnessSummaryResponse response = wellnessActivityService.getWellnessSummary(userId);
        return ResponseEntity.ok(response);
    }
}
