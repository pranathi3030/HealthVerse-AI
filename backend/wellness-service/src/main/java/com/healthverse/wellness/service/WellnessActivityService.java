package com.healthverse.wellness.service;

import com.healthverse.wellness.dto.WellnessActivityRequest;
import com.healthverse.wellness.dto.WellnessActivityResponse;
import com.healthverse.wellness.dto.WellnessSummaryResponse;
import com.healthverse.wellness.entity.WellnessActivity;
import com.healthverse.wellness.exception.ResourceNotFoundException;
import com.healthverse.wellness.repository.WellnessActivityRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WellnessActivityService {

    private final WellnessActivityRepository wellnessActivityRepository;

    public WellnessActivityService(WellnessActivityRepository wellnessActivityRepository) {
        this.wellnessActivityRepository = wellnessActivityRepository;
    }

    public WellnessActivityResponse createWellnessActivity(Long userId, WellnessActivityRequest request) {
        WellnessActivity activity = WellnessActivity.builder()
                .userId(userId)
                .wellnessPlanId(request.getWellnessPlanId())
                .activityName(request.getActivityName())
                .activityType(request.getActivityType())
                .durationMinutes(request.getDurationMinutes())
                .activityDate(request.getActivityDate() != null ? request.getActivityDate() : LocalDate.now())
                .completed(request.getCompleted() != null ? request.getCompleted() : false)
                .moodBefore(request.getMoodBefore())
                .moodAfter(request.getMoodAfter())
                .notes(request.getNotes())
                .build();
        activity = wellnessActivityRepository.save(activity);
        return mapToResponse(activity);
    }

    public List<WellnessActivityResponse> getAllWellnessActivitiesForUser(Long userId) {
        return wellnessActivityRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WellnessActivityResponse getWellnessActivityByIdForUser(Long id, Long userId) {
        WellnessActivity activity = wellnessActivityRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness activity not found or you don't have access"));
        return mapToResponse(activity);
    }

    public WellnessActivityResponse updateWellnessActivity(Long id, Long userId, WellnessActivityRequest request) {
        WellnessActivity activity = wellnessActivityRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness activity not found or you don't have access"));

        if (request.getWellnessPlanId() != null) activity.setWellnessPlanId(request.getWellnessPlanId());
        if (request.getActivityName() != null) activity.setActivityName(request.getActivityName());
        if (request.getActivityType() != null) activity.setActivityType(request.getActivityType());
        if (request.getDurationMinutes() != null) activity.setDurationMinutes(request.getDurationMinutes());
        if (request.getActivityDate() != null) activity.setActivityDate(request.getActivityDate());
        if (request.getCompleted() != null) activity.setCompleted(request.getCompleted());
        if (request.getMoodBefore() != null) activity.setMoodBefore(request.getMoodBefore());
        if (request.getMoodAfter() != null) activity.setMoodAfter(request.getMoodAfter());
        if (request.getNotes() != null) activity.setNotes(request.getNotes());

        activity = wellnessActivityRepository.save(activity);
        return mapToResponse(activity);
    }

    public WellnessActivityResponse markActivityCompleted(Long id, Long userId) {
        WellnessActivity activity = wellnessActivityRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness activity not found or you don't have access"));
        activity.setCompleted(true);
        activity = wellnessActivityRepository.save(activity);
        return mapToResponse(activity);
    }

    public void deleteWellnessActivity(Long id, Long userId) {
        WellnessActivity activity = wellnessActivityRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wellness activity not found or you don't have access"));
        wellnessActivityRepository.delete(activity);
    }

    public List<WellnessActivityResponse> getWellnessActivitiesByDate(Long userId, LocalDate date) {
        return wellnessActivityRepository.findByUserIdAndActivityDate(userId, date)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<WellnessActivityResponse> getCompletedActivities(Long userId) {
        return wellnessActivityRepository.findByUserIdAndCompletedIsTrue(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public WellnessSummaryResponse getWellnessSummary(Long userId) {
        List<WellnessActivity> allActivities = wellnessActivityRepository.findByUserId(userId);
        
        int totalActivities = allActivities.size();
        List<WellnessActivity> completedActivities = allActivities.stream()
                .filter(a -> Boolean.TRUE.equals(a.getCompleted()))
                .collect(Collectors.toList());
        int numCompleted = completedActivities.size();
        
        double completionRate = totalActivities > 0 ? (double) numCompleted / totalActivities : 0.0;
        
        int totalMinutes = completedActivities.stream()
                .filter(a -> a.getDurationMinutes() != null)
                .mapToInt(WellnessActivity::getDurationMinutes)
                .sum();
                
        double avgMoodBefore = completedActivities.stream()
                .filter(a -> a.getMoodBefore() != null)
                .mapToInt(WellnessActivity::getMoodBefore)
                .average().orElse(0.0);
                
        double avgMoodAfter = completedActivities.stream()
                .filter(a -> a.getMoodAfter() != null)
                .mapToInt(WellnessActivity::getMoodAfter)
                .average().orElse(0.0);

        String mostCommonActivity = "None";
        if (!completedActivities.isEmpty()) {
            mostCommonActivity = completedActivities.stream()
                .filter(a -> a.getActivityType() != null)
                .collect(Collectors.groupingBy(WellnessActivity::getActivityType, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
        }
        
        int streak = calculateStreak(completedActivities);

        return WellnessSummaryResponse.builder()
                .totalActivities(totalActivities)
                .completedActivities(numCompleted)
                .completionRate(Math.round(completionRate * 100.0) / 100.0)
                .totalMinutes(totalMinutes)
                .currentStreak(streak)
                .mostCommonActivity(mostCommonActivity)
                .averageMoodBefore(Math.round(avgMoodBefore * 10.0) / 10.0)
                .averageMoodAfter(Math.round(avgMoodAfter * 10.0) / 10.0)
                .build();
    }
    
    private int calculateStreak(List<WellnessActivity> completedActivities) {
        if (completedActivities.isEmpty()) return 0;
        
        List<LocalDate> dates = completedActivities.stream()
                .filter(a -> a.getActivityDate() != null)
                .map(WellnessActivity::getActivityDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
                
        if (dates.isEmpty()) return 0;
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        int streak = 0;
        LocalDate expectedDate = dates.get(0);
        
        if (!expectedDate.equals(today) && !expectedDate.equals(yesterday)) {
            return 0; // Streak broken if no activity today or yesterday
        }
        
        for (LocalDate date : dates) {
            if (date.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (date.isBefore(expectedDate)) {
                break; // Gap found
            }
        }
        return streak;
    }

    private WellnessActivityResponse mapToResponse(WellnessActivity activity) {
        return WellnessActivityResponse.builder()
                .id(activity.getId())
                .userId(activity.getUserId())
                .wellnessPlanId(activity.getWellnessPlanId())
                .activityName(activity.getActivityName())
                .activityType(activity.getActivityType())
                .durationMinutes(activity.getDurationMinutes())
                .activityDate(activity.getActivityDate())
                .completed(activity.getCompleted())
                .moodBefore(activity.getMoodBefore())
                .moodAfter(activity.getMoodAfter())
                .notes(activity.getNotes())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .build();
    }
}
