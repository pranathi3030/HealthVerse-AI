package com.healthverse.nutritionfitness.repository;

import com.healthverse.nutritionfitness.entity.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findByUserId(Long userId);
    Optional<NutritionPlan> findByIdAndUserId(Long id, Long userId);
}
