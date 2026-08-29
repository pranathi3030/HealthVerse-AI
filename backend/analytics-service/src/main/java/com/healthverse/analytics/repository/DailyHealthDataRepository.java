package com.healthverse.analytics.repository;

import com.healthverse.analytics.entity.DailyHealthData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DailyHealthDataRepository extends JpaRepository<DailyHealthData, Long> {
    List<DailyHealthData> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate startDate, LocalDate endDate);
}
