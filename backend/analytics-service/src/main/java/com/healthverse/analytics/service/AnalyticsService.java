package com.healthverse.analytics.service;

import com.healthverse.analytics.dto.HealthScoreResponse;
import com.healthverse.analytics.dto.TrendPoint;
import com.healthverse.analytics.dto.TrendResponse;
import com.healthverse.analytics.dto.WeeklySummaryResponse;
import com.healthverse.analytics.entity.DailyHealthData;
import com.healthverse.analytics.entity.HealthProfile;
import com.healthverse.analytics.repository.DailyHealthDataRepository;
import com.healthverse.analytics.repository.HealthProfileRepository;
import com.healthverse.analytics.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final DailyHealthDataRepository dailyHealthDataRepository;
    private final HealthProfileRepository healthProfileRepository;
    private final MedicineRepository medicineRepository;

    public AnalyticsService(DailyHealthDataRepository dailyHealthDataRepository,
                            HealthProfileRepository healthProfileRepository,
                            MedicineRepository medicineRepository) {
        this.dailyHealthDataRepository = dailyHealthDataRepository;
        this.healthProfileRepository = healthProfileRepository;
        this.medicineRepository = medicineRepository;
    }

    public HealthScoreResponse getHealthScore(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailyHealthData> weeklyData = dailyHealthDataRepository.findByUserIdAndDateBetweenOrderByDateAsc(
                userId, today.minusDays(7), today);

        int score = 70; // Base score

        if (!weeklyData.isEmpty()) {
            double avgSleep = weeklyData.stream().filter(d -> d.getSleepHours() != null).mapToDouble(DailyHealthData::getSleepHours).average().orElse(0);
            double avgWater = weeklyData.stream().filter(d -> d.getWaterIntake() != null).mapToDouble(DailyHealthData::getWaterIntake).average().orElse(0);
            double avgSteps = weeklyData.stream().filter(d -> d.getSteps() != null).mapToInt(DailyHealthData::getSteps).average().orElse(0);

            if (avgSleep >= 7.0) score += 10;
            if (avgWater >= 2.0) score += 10;
            if (avgSteps >= 8000) score += 10;
        }

        Optional<HealthProfile> profileOpt = healthProfileRepository.findByUserId(userId);
        if (profileOpt.isPresent() && profileOpt.get().getBmi() != null) {
            double bmi = profileOpt.get().getBmi();
            if (bmi < 18.5 || bmi > 25.0) {
                score -= 10;
            }
        }

        score = Math.min(100, Math.max(0, score));

        String category;
        if (score >= 90) category = "Excellent";
        else if (score >= 75) category = "Good";
        else if (score >= 60) category = "Fair";
        else category = "Needs Improvement";

        return HealthScoreResponse.builder()
                .score(score)
                .category(category)
                .disclaimer("This score is a mathematical aggregation and does not constitute medical advice or diagnosis.")
                .build();
    }

    public WeeklySummaryResponse getWeeklySummary(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailyHealthData> weeklyData = dailyHealthDataRepository.findByUserIdAndDateBetweenOrderByDateAsc(
                userId, today.minusDays(7), today);

        double avgSleep = weeklyData.stream().filter(d -> d.getSleepHours() != null).mapToDouble(DailyHealthData::getSleepHours).average().orElse(0);
        double avgWater = weeklyData.stream().filter(d -> d.getWaterIntake() != null).mapToDouble(DailyHealthData::getWaterIntake).average().orElse(0);
        int avgSteps = (int) weeklyData.stream().filter(d -> d.getSteps() != null).mapToInt(DailyHealthData::getSteps).average().orElse(0);

        int activeMedicines = medicineRepository.countByUserIdAndActiveTrue(userId);

        return WeeklySummaryResponse.builder()
                .avgSleepHours(Math.round(avgSleep * 10.0) / 10.0)
                .avgWaterIntake(Math.round(avgWater * 10.0) / 10.0)
                .avgSteps(avgSteps)
                .activeMedicines(activeMedicines)
                .build();
    }

    public TrendResponse getTrends(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailyHealthData> monthlyData = dailyHealthDataRepository.findByUserIdAndDateBetweenOrderByDateAsc(
                userId, today.minusDays(30), today);

        List<TrendPoint> points = monthlyData.stream().map(d -> TrendPoint.builder()
                .date(d.getDate())
                .weight(d.getWeight())
                .sleepHours(d.getSleepHours())
                .steps(d.getSteps())
                .waterIntake(d.getWaterIntake())
                .build()
        ).collect(Collectors.toList());

        return TrendResponse.builder().trends(points).build();
    }
}
