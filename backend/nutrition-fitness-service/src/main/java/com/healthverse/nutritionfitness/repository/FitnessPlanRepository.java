package com.healthverse.nutritionfitness.repository;

import com.healthverse.nutritionfitness.entity.FitnessPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessPlanRepository extends JpaRepository<FitnessPlan, Long> {
    List<FitnessPlan> findByUserId(Long userId);
    Optional<FitnessPlan> findByIdAndUserId(Long id, Long userId);
}
