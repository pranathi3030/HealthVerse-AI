package com.healthverse.wellness.dto;

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
public class WellnessActivityResponse {
    private Long id;
    private Long userId;
    private Long wellnessPlanId;
    private String activityName;
    private String activityType;
    private Integer durationMinutes;
    private LocalDate activityDate;
    private Boolean completed;
    private Integer moodBefore;
    private Integer moodAfter;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
