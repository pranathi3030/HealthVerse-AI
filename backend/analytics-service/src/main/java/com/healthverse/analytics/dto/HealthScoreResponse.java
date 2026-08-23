package com.healthverse.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthScoreResponse {
    private Integer score;
    private String category;
    private String disclaimer;
}
