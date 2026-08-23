package com.healthverse.medicine.repository;

import com.healthverse.medicine.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByUserId(Long userId);
    Optional<Medicine> findByIdAndUserId(Long id, Long userId);
}
