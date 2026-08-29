package com.healthverse.user.controller;

import com.healthverse.user.dto.DailyHealthDataDto;
import com.healthverse.user.dto.DailyHealthDataRequest;
import com.healthverse.user.service.DailyHealthDataService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health/daily")
public class DailyHealthDataController {

    private final DailyHealthDataService service;

    public DailyHealthDataController(DailyHealthDataService service) {
        this.service = service;
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new com.healthverse.user.exception.BadRequestException("User is not authenticated");
        }

        if (authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        
        throw new com.healthverse.user.exception.BadRequestException("Unable to extract userId from authentication token");
    }

    @PostMapping
    public ResponseEntity<DailyHealthDataDto> createDailyData(
            Authentication authentication,
            @Valid @RequestBody DailyHealthDataRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        DailyHealthDataDto dto = service.createDailyData(userId, request);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<DailyHealthDataDto> getTodayData(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        DailyHealthDataDto dto = service.getDailyData(userId, LocalDate.now());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{date}")
    public ResponseEntity<DailyHealthDataDto> getDailyDataByDate(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = extractUserIdFromAuth(authentication);
        DailyHealthDataDto dto = service.getDailyData(userId, date);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/history")
    public ResponseEntity<List<DailyHealthDataDto>> getDailyDataHistory(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<DailyHealthDataDto> history = service.getDailyDataHistory(userId);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{date}")
    public ResponseEntity<DailyHealthDataDto> updateDailyData(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody DailyHealthDataRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        DailyHealthDataDto dto = service.updateDailyData(userId, date, request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> deleteDailyData(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = extractUserIdFromAuth(authentication);
        service.deleteDailyData(userId, date);
        return ResponseEntity.noContent().build();
    }
}
