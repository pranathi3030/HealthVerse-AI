package com.healthverse.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHealthProfileRequest {

    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;

    private String gender;

    @Min(value = 30, message = "Height must be at least 30 cm")
    @Max(value = 300, message = "Height must be less than 300 cm")
    private Double height; // in cm

    @Min(value = 10, message = "Weight must be at least 10 kg")
    @Max(value = 500, message = "Weight must be less than 500 kg")
    private Double weight; // in kg

    private String lifestyle;

    private String goals;

    private String allergies;

    private String conditions;
}
