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
public class NutritionPlanResponse {

    private Long id;
    private Long userId;
    private String planName;
    private Integer dailyCalories;
    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;
    private Double waterLiters;
    private String mealPlan;
    private String dietType;
    private String allergyRestrictions;
    private Integer durationDays;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
