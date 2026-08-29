package com.healthverse.analytics;

import com.healthverse.analytics.dto.HealthScoreResponse;
import com.healthverse.analytics.entity.DailyHealthData;
import com.healthverse.analytics.entity.HealthProfile;
import com.healthverse.analytics.repository.DailyHealthDataRepository;
import com.healthverse.analytics.repository.HealthProfileRepository;
import com.healthverse.analytics.repository.MedicineRepository;
import com.healthverse.analytics.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {

    @Mock
    private DailyHealthDataRepository dailyHealthDataRepository;

    @Mock
    private HealthProfileRepository healthProfileRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHealthScore_Excellent() {
        DailyHealthData data1 = new DailyHealthData();
        data1.setSleepHours(8.0);
        data1.setWaterIntake(2.5);
        data1.setSteps(10000);

        HealthProfile profile = new HealthProfile();
        // BMI between 18.5 and 25
        try {
            java.lang.reflect.Field bmiField = profile.getClass().getDeclaredField("bmi");
            bmiField.setAccessible(true);
            bmiField.set(profile, 22.0);
        } catch (Exception e) {}

        when(dailyHealthDataRepository.findByUserIdAndDateBetweenOrderByDateAsc(eq(1L), any(), any()))
                .thenReturn(Arrays.asList(data1));
        when(healthProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        HealthScoreResponse response = analyticsService.getHealthScore(1L);

        // Base 70 + 10 (sleep) + 10 (water) + 10 (steps) = 100
        assertEquals(100, response.getScore());
        assertEquals("Excellent", response.getCategory());
    }

    @Test
    void testGetHealthScore_NeedsImprovement() {
        DailyHealthData data1 = new DailyHealthData();
        data1.setSleepHours(5.0);
        data1.setWaterIntake(1.0);
        data1.setSteps(3000);

        HealthProfile profile = new HealthProfile();
        // BMI > 25
        try {
            java.lang.reflect.Field bmiField = profile.getClass().getDeclaredField("bmi");
            bmiField.setAccessible(true);
            bmiField.set(profile, 30.0);
        } catch (Exception e) {}

        when(dailyHealthDataRepository.findByUserIdAndDateBetweenOrderByDateAsc(eq(1L), any(), any()))
                .thenReturn(Arrays.asList(data1));
        when(healthProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        HealthScoreResponse response = analyticsService.getHealthScore(1L);

        // Base 70 - 10 (BMI penalty) = 60
        assertEquals(60, response.getScore());
        assertEquals("Fair", response.getCategory());
    }
}
