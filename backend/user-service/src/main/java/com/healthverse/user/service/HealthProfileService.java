package com.healthverse.user.service;

import com.healthverse.user.dto.HealthProfileDto;
import com.healthverse.user.dto.UpdateHealthProfileRequest;

public interface HealthProfileService {

    HealthProfileDto getHealthProfile(String email, Long tokenUserId);

    HealthProfileDto createOrUpdateHealthProfile(String email, Long tokenUserId, UpdateHealthProfileRequest request);
}
