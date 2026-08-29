package com.healthverse.medicine.service.impl;

import com.healthverse.medicine.dto.MedicineRequest;
import com.healthverse.medicine.dto.MedicineResponse;
import com.healthverse.medicine.entity.Medicine;
import com.healthverse.medicine.exception.ResourceNotFoundException;
import com.healthverse.medicine.repository.MedicineRepository;
import com.healthverse.medicine.service.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository repository;

    public MedicineServiceImpl(MedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public MedicineResponse createMedicine(Long userId, MedicineRequest request) {
        Medicine medicine = Medicine.builder()
                .userId(userId)
                .name(request.getName())
                .dosage(request.getDosage())
                .frequency(request.getFrequency())
                .instructions(request.getInstructions())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Medicine saved = repository.save(medicine);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> getAllMedicinesForUser(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getMedicineByIdForUser(Long id, Long userId) {
        Medicine medicine = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return mapToResponse(medicine);
    }

    @Override
    public MedicineResponse updateMedicine(Long id, Long userId, MedicineRequest request) {
        Medicine medicine = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setName(request.getName());
        medicine.setDosage(request.getDosage());
        medicine.setFrequency(request.getFrequency());
        medicine.setInstructions(request.getInstructions());
        medicine.setStartDate(request.getStartDate());
        medicine.setEndDate(request.getEndDate());
        if (request.getActive() != null) {
            medicine.setActive(request.getActive());
        }

        Medicine saved = repository.save(medicine);
        return mapToResponse(saved);
    }

    @Override
    public void deleteMedicine(Long id, Long userId) {
        Medicine medicine = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        repository.delete(medicine);
    }

    private MedicineResponse mapToResponse(Medicine entity) {
        return MedicineResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .name(entity.getName())
                .dosage(entity.getDosage())
                .frequency(entity.getFrequency())
                .instructions(entity.getInstructions())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
