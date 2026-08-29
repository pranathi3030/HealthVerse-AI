package com.healthverse.medicine.controller;

import com.healthverse.medicine.dto.MedicineRequest;
import com.healthverse.medicine.dto.MedicineResponse;
import com.healthverse.medicine.exception.BadRequestException;
import com.healthverse.medicine.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("User is not authenticated");
        }
        if (authentication.getCredentials() instanceof Long userId) {
            return userId;
        }
        throw new BadRequestException("Unable to extract userId from authentication token");
    }

    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(
            Authentication authentication,
            @Valid @RequestBody MedicineRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        MedicineResponse response = medicineService.createMedicine(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAllMedicines(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<MedicineResponse> responses = medicineService.getAllMedicinesForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicineById(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        MedicineResponse response = medicineService.getMedicineByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequest request) {
        Long userId = extractUserIdFromAuth(authentication);
        MedicineResponse response = medicineService.updateMedicine(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(
            Authentication authentication,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth(authentication);
        medicineService.deleteMedicine(id, userId);
        return ResponseEntity.noContent().build();
    }
}
