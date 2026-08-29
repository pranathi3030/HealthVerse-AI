package com.healthverse.user.service.impl;

import com.healthverse.user.dto.DailyHealthDataDto;
import com.healthverse.user.dto.DailyHealthDataRequest;
import com.healthverse.user.entity.DailyHealthData;
import com.healthverse.user.exception.BadRequestException;
import com.healthverse.user.exception.ResourceNotFoundException;
import com.healthverse.user.repository.DailyHealthDataRepository;
import com.healthverse.user.service.DailyHealthDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyHealthDataServiceImpl implements DailyHealthDataService {

    private final DailyHealthDataRepository repository;

    public DailyHealthDataServiceImpl(DailyHealthDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public DailyHealthDataDto createDailyData(Long userId, DailyHealthDataRequest request) {
        if (repository.findByUserIdAndDate(userId, request.getDate()).isPresent()) {
            throw new BadRequestException("Daily health data already exists for this date.");
        }

        DailyHealthData data = DailyHealthData.builder()
                .userId(userId)
                .date(request.getDate())
                .weight(request.getWeight())
                .sleepHours(request.getSleepHours())
                .steps(request.getSteps())
                .waterIntake(request.getWaterIntake())
                .exerciseMinutes(request.getExerciseMinutes())
                .mood(request.getMood())
                .build();

        DailyHealthData saved = repository.save(data);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyHealthDataDto getDailyData(Long userId, LocalDate date) {
        DailyHealthData data = repository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Daily health data not found for date: " + date));
        return mapToDto(data);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyHealthDataDto> getDailyDataHistory(Long userId) {
        return repository.findByUserIdOrderByDateDesc(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DailyHealthDataDto updateDailyData(Long userId, LocalDate date, DailyHealthDataRequest request) {
        if (!date.equals(request.getDate())) {
            throw new BadRequestException("Date in path must match date in body.");
        }

        DailyHealthData data = repository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Daily health data not found for date: " + date));

        data.setWeight(request.getWeight());
        data.setSleepHours(request.getSleepHours());
        data.setSteps(request.getSteps());
        data.setWaterIntake(request.getWaterIntake());
        data.setExerciseMinutes(request.getExerciseMinutes());
        data.setMood(request.getMood());

        DailyHealthData saved = repository.save(data);
        return mapToDto(saved);
    }

    @Override
    public void deleteDailyData(Long userId, LocalDate date) {
        DailyHealthData data = repository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Daily health data not found for date: " + date));
        repository.delete(data);
    }

    private DailyHealthDataDto mapToDto(DailyHealthData entity) {
        return DailyHealthDataDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .date(entity.getDate())
                .weight(entity.getWeight())
                .sleepHours(entity.getSleepHours())
                .steps(entity.getSteps())
                .waterIntake(entity.getWaterIntake())
                .exerciseMinutes(entity.getExerciseMinutes())
                .mood(entity.getMood())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
