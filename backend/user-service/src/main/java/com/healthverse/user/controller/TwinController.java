package com.healthverse.user.controller;

import com.healthverse.user.dto.DigitalHealthTwinDto;
import com.healthverse.user.dto.TwinContextDto;
import com.healthverse.user.service.TwinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-twin")
@RequiredArgsConstructor
public class TwinController {

    private final TwinService twinService;

    @GetMapping("/status")
    public ResponseEntity<DigitalHealthTwinDto> getTwinStatus(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(twinService.getTwinMetrics(userId));
    }

    @GetMapping("/context")
    public ResponseEntity<TwinContextDto> getTwinContext(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(twinService.getTwinContext(userId));
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication != null && authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        return null;
    }
}
