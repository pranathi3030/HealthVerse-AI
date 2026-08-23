package com.healthverse.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrendResponse {
    private List<TrendPoint> trends;
}
