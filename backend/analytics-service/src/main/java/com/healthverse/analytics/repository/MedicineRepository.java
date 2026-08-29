package com.healthverse.analytics.repository;

import com.healthverse.analytics.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    int countByUserIdAndActiveTrue(Long userId);
}
