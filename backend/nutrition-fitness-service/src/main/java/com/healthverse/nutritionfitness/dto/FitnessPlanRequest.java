package com.healthverse.nutritionfitness.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlanRequest {

    private String goal;
    private String fitnessLevel;

    @Min(value = 1, message = "weeklyFrequency must be greater than 0")
    private Integer weeklyFrequency;

    @Min(value = 10, message = "sessionDurationMinutes must be at least 10")
    private Integer sessionDurationMinutes;

    @Min(value = 1, message = "durationDays must be greater than 0")
    private Integer durationDays;
}
