package com.healthverse.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "medicines")
@Getter
public class Medicine {
    @Id
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private Boolean active;
}
