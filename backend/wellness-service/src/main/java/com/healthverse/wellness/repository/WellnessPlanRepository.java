package com.healthverse.wellness.repository;

import com.healthverse.wellness.entity.WellnessPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WellnessPlanRepository extends JpaRepository<WellnessPlan, Long> {
    List<WellnessPlan> findByUserId(Long userId);
    Optional<WellnessPlan> findByIdAndUserId(Long id, Long userId);
}
