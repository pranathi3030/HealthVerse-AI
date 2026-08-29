package com.healthverse.wellness.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthProfileDto {
    private Long id;
    private Long userId;
    private Integer age;
    private String gender;
    private Double height;
    private Double weight;
    private Double bmi;
    private String bmiCategory;
    private String lifestyle;
    private String goals;
    private String allergies;
    private String conditions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
