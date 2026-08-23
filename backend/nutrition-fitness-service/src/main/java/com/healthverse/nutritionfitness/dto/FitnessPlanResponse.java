package com.healthverse.nutritionfitness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlanResponse {

    private Long id;
    private Long userId;
    private String planName;
    private String goal;
    private String fitnessLevel;
    private Integer weeklyFrequency;
    private Integer sessionDurationMinutes;
    private String workoutPlan;
    private Integer calorieBurnTarget;
    private Integer durationDays;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
