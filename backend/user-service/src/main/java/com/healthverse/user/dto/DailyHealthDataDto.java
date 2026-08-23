package com.healthverse.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyHealthDataDto {
    private Long id;
    private Long userId;
    private LocalDate date;
    private Double weight;
    private Double sleepHours;
    private Integer steps;
    private Double waterIntake;
    private Integer exerciseMinutes;
    private String mood;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
