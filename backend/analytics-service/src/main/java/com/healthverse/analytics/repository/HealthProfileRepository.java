package com.healthverse.analytics.repository;

import com.healthverse.analytics.entity.HealthProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    Optional<HealthProfile> findByUserId(Long userId);
}
