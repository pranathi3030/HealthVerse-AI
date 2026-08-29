package com.healthverse.nutritionfitness.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nutrition_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String planName;
    private Integer dailyCalories;
    private Integer proteinGrams;
    private Integer carbsGrams;
    private Integer fatGrams;
    private Double waterLiters;
    
    @Column(columnDefinition = "TEXT")
    private String mealPlan;
    
    private String dietType;
    private String allergyRestrictions;
    private Integer durationDays;
    private Boolean active;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
