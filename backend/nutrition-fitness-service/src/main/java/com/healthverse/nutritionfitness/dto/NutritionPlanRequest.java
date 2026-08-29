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
public class NutritionPlanRequest {

    private String dietType;

    @Min(value = 1, message = "durationDays must be greater than 0")
    private Integer durationDays;

    private String additionalPreferences;
}
