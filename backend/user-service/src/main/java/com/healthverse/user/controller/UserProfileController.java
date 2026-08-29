package com.healthverse.user.controller;

import com.healthverse.user.dto.ApiResponse;
import com.healthverse.user.dto.UpdateUserProfileRequest;
import com.healthverse.user.dto.UserProfileDto;
import com.healthverse.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {
        String email = authentication.getName();
        UserProfileDto profile = userService.getUserProfile(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        String email = authentication.getName();
        UserProfileDto updated = userService.updateUserProfile(email, request);
        return ResponseEntity.ok(updated);
    }
}
