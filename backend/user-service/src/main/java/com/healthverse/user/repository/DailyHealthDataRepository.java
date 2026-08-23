package com.healthverse.user.repository;

import com.healthverse.user.entity.DailyHealthData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyHealthDataRepository extends JpaRepository<DailyHealthData, Long> {
    Optional<DailyHealthData> findByUserIdAndDate(Long userId, LocalDate date);
    List<DailyHealthData> findByUserIdOrderByDateDesc(Long userId);
}
