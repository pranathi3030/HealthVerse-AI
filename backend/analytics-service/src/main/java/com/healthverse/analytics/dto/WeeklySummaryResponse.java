package com.healthverse.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklySummaryResponse {
    private Double avgSleepHours;
    private Double avgWaterIntake;
    private Integer avgSteps;
    private int activeMedicines;
}
