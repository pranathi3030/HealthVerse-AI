package com.healthverse.wellness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WellnessPlanResponse {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String category;
    private String goal;
    private Integer durationDays;
    private Integer dailyMinutes;
    private String priority;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
