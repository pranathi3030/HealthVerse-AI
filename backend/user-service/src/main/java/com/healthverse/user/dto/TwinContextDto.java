package com.healthverse.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwinContextDto {
    private Long userId;
    private String name;
    private Integer age;
    private String gender;
    private String conditions;
    private String allergies;
    private String lifestyle;
    private String goals;
    private Map<String, Object> recentMetrics;
}
