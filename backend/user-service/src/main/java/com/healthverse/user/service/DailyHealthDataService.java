package com.healthverse.user.service;

import com.healthverse.user.dto.DailyHealthDataDto;
import com.healthverse.user.dto.DailyHealthDataRequest;

import java.time.LocalDate;
import java.util.List;

public interface DailyHealthDataService {
    DailyHealthDataDto createDailyData(Long userId, DailyHealthDataRequest request);
    DailyHealthDataDto getDailyData(Long userId, LocalDate date);
    List<DailyHealthDataDto> getDailyDataHistory(Long userId);
    DailyHealthDataDto updateDailyData(Long userId, LocalDate date, DailyHealthDataRequest request);
    void deleteDailyData(Long userId, LocalDate date);
}
