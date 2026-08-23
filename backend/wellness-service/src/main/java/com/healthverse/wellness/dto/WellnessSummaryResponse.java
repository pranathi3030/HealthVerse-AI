package com.healthverse.wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessSummaryResponse {
    private Integer totalActivities;
    private Integer completedActivities;
    private Double completionRate;
    private Integer totalMinutes;
    private Integer currentStreak;
    private String mostCommonActivity;
    private Double averageMoodBefore;
    private Double averageMoodAfter;
}
