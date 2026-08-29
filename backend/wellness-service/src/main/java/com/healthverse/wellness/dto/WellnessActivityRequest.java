package com.healthverse.wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessActivityRequest {
    private Long wellnessPlanId;
    private String activityName;
    private String activityType;
    private Integer durationMinutes;
    private LocalDate activityDate;
    private Boolean completed;
    private Integer moodBefore;
    private Integer moodAfter;
    private String notes;
}
