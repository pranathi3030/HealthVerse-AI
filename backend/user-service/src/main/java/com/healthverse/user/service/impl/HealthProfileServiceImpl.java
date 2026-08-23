package com.healthverse.user.service.impl;

import com.healthverse.user.dto.HealthProfileDto;
import com.healthverse.user.dto.UpdateHealthProfileRequest;
import com.healthverse.user.entity.HealthProfile;
import com.healthverse.user.entity.User;
import com.healthverse.user.exception.ResourceNotFoundException;
import com.healthverse.user.repository.HealthProfileRepository;
import com.healthverse.user.repository.UserRepository;
import com.healthverse.user.service.HealthProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HealthProfileServiceImpl implements HealthProfileService {

    private final HealthProfileRepository healthProfileRepository;
    private final UserRepository userRepository;

    public HealthProfileServiceImpl(HealthProfileRepository healthProfileRepository,
                                    UserRepository userRepository) {
        this.healthProfileRepository = healthProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public HealthProfileDto getHealthProfile(String email, Long tokenUserId) {
        Long userId = resolveUserId(email, tokenUserId);

        HealthProfile profile = healthProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Health profile not found for user ID: " + userId));

        return mapToDto(profile);
    }

    @Override
    @Transactional
    public HealthProfileDto createOrUpdateHealthProfile(String email, Long tokenUserId, UpdateHealthProfileRequest request) {
        Long userId = resolveUserId(email, tokenUserId);

        HealthProfile profile = healthProfileRepository.findByUserId(userId)
                .orElseGet(() -> HealthProfile.builder().userId(userId).build());

        if (request.getAge() != null) {
            profile.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            profile.setGender(request.getGender().trim());
        }
        if (request.getHeight() != null) {
            profile.setHeight(request.getHeight());
        }
        if (request.getWeight() != null) {
            profile.setWeight(request.getWeight());
        }
        if (request.getLifestyle() != null) {
            profile.setLifestyle(request.getLifestyle().trim());
        }
        if (request.getGoals() != null) {
            profile.setGoals(request.getGoals().trim());
        }
        if (request.getAllergies() != null) {
            profile.setAllergies(request.getAllergies().trim());
        }
        if (request.getConditions() != null) {
            profile.setConditions(request.getConditions().trim());
        }

        // Calculate and update BMI if height and weight are available
        if (profile.getHeight() != null && profile.getWeight() != null && profile.getHeight() > 0) {
            double heightInMeters = profile.getHeight() / 100.0;
            double bmi = profile.getWeight() / (heightInMeters * heightInMeters);
            profile.setBmi(Math.round(bmi * 100.0) / 100.0);
        }

        HealthProfile saved = healthProfileRepository.save(profile);
        return mapToDto(saved);
    }

    private Long resolveUserId(String email, Long tokenUserId) {
        if (tokenUserId != null) {
            return tokenUserId;
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return user.getId();
    }

    private HealthProfileDto mapToDto(HealthProfile profile) {
        String bmiCategory = null;
        if (profile.getBmi() != null) {
            double bmi = profile.getBmi();
            if (bmi < 18.5) {
                bmiCategory = "Underweight";
            } else if (bmi < 25.0) {
                bmiCategory = "Normal weight";
            } else if (bmi < 30.0) {
                bmiCategory = "Overweight";
            } else {
                bmiCategory = "Obese";
            }
        }

        return HealthProfileDto.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .age(profile.getAge())
                .gender(profile.getGender())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .bmi(profile.getBmi())
                .bmiCategory(bmiCategory)
                .lifestyle(profile.getLifestyle())
                .goals(profile.getGoals())
                .allergies(profile.getAllergies())
                .conditions(profile.getConditions())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
