package com.healthverse.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyHealthDataRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Positive(message = "Weight must be positive")
    private Double weight;

    @Min(value = 0, message = "Sleep hours cannot be negative")
    @Max(value = 24, message = "Sleep hours cannot exceed 24")
    private Double sleepHours;

    @Min(value = 0, message = "Steps cannot be negative")
    private Integer steps;

    @Min(value = 0, message = "Water intake cannot be negative")
    private Double waterIntake;

    @Min(value = 0, message = "Exercise minutes cannot be negative")
    private Integer exerciseMinutes;

    @Size(max = 100, message = "Mood should not exceed 100 characters")
    private String mood;
}
