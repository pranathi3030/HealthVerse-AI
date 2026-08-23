package com.healthverse.wellness.repository;

import com.healthverse.wellness.entity.WellnessActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WellnessActivityRepository extends JpaRepository<WellnessActivity, Long> {
    List<WellnessActivity> findByUserId(Long userId);
    Optional<WellnessActivity> findByIdAndUserId(Long id, Long userId);
    List<WellnessActivity> findByUserIdAndCompletedIsTrue(Long userId);
    List<WellnessActivity> findByUserIdAndActivityDate(Long userId, LocalDate date);
}
