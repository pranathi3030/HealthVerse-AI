package com.healthverse.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "daily_health_data")
@Getter
@Setter
public class DailyHealthData {

    @Id
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private LocalDate date;
    private Double weight;
    @Column(name = "sleep_hours")
    private Double sleepHours;
    private Integer steps;
    @Column(name = "water_intake")
    private Double waterIntake;
    @Column(name = "exercise_minutes")
    private Integer exerciseMinutes;
}
