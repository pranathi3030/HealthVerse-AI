package com.healthverse.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "health_profiles")
@Getter
public class HealthProfile {
    @Id
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private Double bmi;
}
