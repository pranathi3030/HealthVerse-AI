package com.healthverse.user.controller;

import com.healthverse.user.dto.HealthProfileDto;
import com.healthverse.user.dto.UpdateHealthProfileRequest;
import com.healthverse.user.service.HealthProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthProfileController {

    private final HealthProfileService healthProfileService;

    public HealthProfileController(HealthProfileService healthProfileService) {
        this.healthProfileService = healthProfileService;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<HealthProfileDto> getHealthProfile(
            @PathVariable Long userId,
            Authentication authentication) {
        String email = authentication.getName();
        Long tokenUserId = extractUserIdFromAuth(authentication);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!userId.equals(tokenUserId) && !isAdmin) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build();
        }

        HealthProfileDto profile = healthProfileService.getHealthProfile(email, userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<HealthProfileDto> updateHealthProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateHealthProfileRequest request) {
        String email = authentication.getName();
        Long tokenUserId = extractUserIdFromAuth(authentication);

        HealthProfileDto profile = healthProfileService.createOrUpdateHealthProfile(email, tokenUserId, request);
        return ResponseEntity.ok(profile);
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        return null;
    }
}
