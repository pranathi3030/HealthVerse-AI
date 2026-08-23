package com.healthverse.wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessPlanRequest {
    private String title;
    private String description;
    private String category;
    private String goal;
    private Integer durationDays;
    private Integer dailyMinutes;
    private String priority;
    private Boolean active;
}
