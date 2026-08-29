package com.healthverse.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TrendPoint {
    private LocalDate date;
    private Double weight;
    private Double sleepHours;
    private Integer steps;
    private Double waterIntake;
}
