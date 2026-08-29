package com.healthverse.user.service.impl;

import com.healthverse.user.dto.DigitalHealthTwinDto;
import com.healthverse.user.dto.TwinContextDto;
import com.healthverse.user.entity.DailyHealthData;
import com.healthverse.user.entity.HealthProfile;
import com.healthverse.user.entity.User;
import com.healthverse.user.repository.DailyHealthDataRepository;
import com.healthverse.user.repository.HealthProfileRepository;
import com.healthverse.user.repository.UserRepository;
import com.healthverse.user.service.TwinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TwinServiceImpl implements TwinService {

    private final HealthProfileRepository healthProfileRepository;
    private final DailyHealthDataRepository dailyHealthDataRepository;
    private final UserRepository userRepository;

    @Override
    public DigitalHealthTwinDto getTwinMetrics(Long userId) {
        List<DailyHealthData> history = dailyHealthDataRepository.findByUserIdOrderByDateDesc(userId);
        
        if (history.isEmpty()) {
            return getEmptyTwinDto();
        }

        List<DigitalHealthTwinDto.WeightTrendDto> weightTrend = new ArrayList<>();
        List<DigitalHealthTwinDto.ActivitySleepTrendDto> activitySleepTrend = new ArrayList<>();
        
        List<DailyHealthData> last7Days = history.stream().limit(7).collect(Collectors.toList());
        Collections.reverse(last7Days);
        
        Optional<HealthProfile> profileOpt = healthProfileRepository.findByUserId(userId);
        double heightInMeters = profileOpt.map(p -> p.getHeight() != null ? p.getHeight() / 100.0 : 1.75).orElse(1.75);
        String conditions = profileOpt.map(HealthProfile::getConditions).orElse("none");
        
        double totalWater = 0;
        int totalSteps = 0;
        double totalSleep = 0;
        int mentalScoreSum = 0;
        int count = last7Days.size();

        for (DailyHealthData data : last7Days) {
            String dayName = data.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            
            double w = data.getWeight() != null ? data.getWeight() : 70.0;
            double bmi = w / (heightInMeters * heightInMeters);
            
            weightTrend.add(DigitalHealthTwinDto.WeightTrendDto.builder()
                    .date(dayName)
                    .weight(Math.round(w * 10.0) / 10.0)
                    .bmi(Math.round(bmi * 10.0) / 10.0)
                    .build());
                    
            activitySleepTrend.add(DigitalHealthTwinDto.ActivitySleepTrendDto.builder()
                    .date(dayName)
                    .activity(data.getSteps() != null ? data.getSteps() : 5000)
                    .sleep(data.getSleepHours() != null ? data.getSleepHours() : 6.0)
                    .build());

            totalWater += data.getWaterIntake() != null ? data.getWaterIntake() : 1.0;
            totalSteps += data.getSteps() != null ? data.getSteps() : 5000;
            totalSleep += data.getSleepHours() != null ? data.getSleepHours() : 6.0;

            String mood = data.getMood() != null ? data.getMood().toLowerCase() : "good";
            if (mood.contains("excellent") || mood.contains("great")) mentalScoreSum += 100;
            else if (mood.contains("good") || mood.contains("okay")) mentalScoreSum += 80;
            else if (mood.contains("average")) mentalScoreSum += 70;
            else mentalScoreSum += 50;
        }
        
        double avgWater = totalWater / count;
        double avgSteps = totalSteps / (double) count;
        double avgSleep = totalSleep / count;
        int avgMental = mentalScoreSum / count;

        int nutritionAdherence = (int) Math.min(100, (avgWater / 2.5) * 100);
        int medicationAdherence = (conditions == null || conditions.equalsIgnoreCase("none")) ? 100 : 95;

        int physical = (int) Math.min(100, (avgSteps / 10000.0) * 100);
        int sleepScore = (int) Math.min(100, (avgSleep / 8.0) * 100);
        int recovery = (sleepScore + nutritionAdherence) / 2;
        int focus = (avgMental + sleepScore) / 2;

        int overallWellnessScore = (physical + avgMental + recovery + sleepScore + nutritionAdherence + focus) / 6;

        List<DigitalHealthTwinDto.RadarDataDto> radarData = Arrays.asList(
            new DigitalHealthTwinDto.RadarDataDto("Physical", physical, 100),
            new DigitalHealthTwinDto.RadarDataDto("Mental", avgMental, 100),
            new DigitalHealthTwinDto.RadarDataDto("Recovery", recovery, 100),
            new DigitalHealthTwinDto.RadarDataDto("Sleep", sleepScore, 100),
            new DigitalHealthTwinDto.RadarDataDto("Nutrition", nutritionAdherence, 100),
            new DigitalHealthTwinDto.RadarDataDto("Focus", focus, 100)
        );

        List<DigitalHealthTwinDto.JourneyEventDto> journeyEvents = new ArrayList<>();
        DailyHealthData latest = history.get(0);
        journeyEvents.add(new DigitalHealthTwinDto.JourneyEventDto("Today", "Health data logged", "success"));
        if (latest.getSteps() != null && latest.getSteps() >= 10000) {
            journeyEvents.add(new DigitalHealthTwinDto.JourneyEventDto("Today", "Hit 10k steps goal", "positive"));
        }
        if (latest.getSleepHours() != null && latest.getSleepHours() < 6) {
            journeyEvents.add(new DigitalHealthTwinDto.JourneyEventDto("Today", "Sleep deficit detected", "warning"));
        }
        if (journeyEvents.size() < 3 && history.size() > 1) {
            journeyEvents.add(new DigitalHealthTwinDto.JourneyEventDto("Yesterday", "Consistent tracking maintained", "info"));
        }

        String status = overallWellnessScore > 80 ? "optimal" : (overallWellnessScore > 60 ? "improving" : "needs_attention");

        return DigitalHealthTwinDto.builder()
                .weightTrend(weightTrend)
                .activitySleepTrend(activitySleepTrend)
                .nutritionAdherence(nutritionAdherence)
                .medicationAdherence(medicationAdherence)
                .overallWellnessScore(overallWellnessScore)
                .status(status)
                .radarData(radarData)
                .journeyEvents(journeyEvents)
                .build();
    }

    private DigitalHealthTwinDto getEmptyTwinDto() {
        return DigitalHealthTwinDto.builder()
                .weightTrend(Collections.emptyList())
                .activitySleepTrend(Collections.emptyList())
                .nutritionAdherence(0)
                .medicationAdherence(0)
                .overallWellnessScore(0)
                .status("insufficient data")
                .radarData(Collections.emptyList())
                .journeyEvents(Collections.emptyList())
                .build();
    }

    @Override
    public TwinContextDto getTwinContext(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        HealthProfile profile = healthProfileRepository.findByUserId(userId).orElse(new HealthProfile());
        
        Map<String, Object> recentMetrics = new HashMap<>();
        dailyHealthDataRepository.findByUserIdOrderByDateDesc(userId).stream().findFirst().ifPresent(data -> {
            recentMetrics.put("weight", data.getWeight());
            recentMetrics.put("sleepHours", data.getSleepHours());
            recentMetrics.put("steps", data.getSteps());
            recentMetrics.put("waterIntake", data.getWaterIntake());
            recentMetrics.put("exerciseMinutes", data.getExerciseMinutes());
            recentMetrics.put("mood", data.getMood());
            recentMetrics.put("date", data.getDate().toString());
        });
        
        return TwinContextDto.builder()
                .userId(userId)
                .name(user.getName())
                .age(profile.getAge())
                .gender(profile.getGender())
                .conditions(profile.getConditions())
                .allergies(profile.getAllergies())
                .lifestyle(profile.getLifestyle())
                .goals(profile.getGoals())
                .recentMetrics(recentMetrics)
                .build();
    }
}
