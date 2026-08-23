package com.healthverse.nutritionfitness.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private String planName;
    private String goal;
    private String fitnessLevel;
    private Integer weeklyFrequency;
    private Integer sessionDurationMinutes;
    
    @Column(columnDefinition = "TEXT")
    private String workoutPlan;
    
    private Integer calorieBurnTarget;
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
