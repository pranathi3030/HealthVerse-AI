package com.healthverse.medicine.service;

import com.healthverse.medicine.dto.MedicineRequest;
import com.healthverse.medicine.dto.MedicineResponse;

import java.util.List;

public interface MedicineService {
    MedicineResponse createMedicine(Long userId, MedicineRequest request);
    List<MedicineResponse> getAllMedicinesForUser(Long userId);
    MedicineResponse getMedicineByIdForUser(Long id, Long userId);
    MedicineResponse updateMedicine(Long id, Long userId, MedicineRequest request);
    void deleteMedicine(Long id, Long userId);
}
