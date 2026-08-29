package com.healthverse.aianalysis.repository;

import com.healthverse.aianalysis.entity.MedicalReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalReportRepository extends MongoRepository<MedicalReport, String> {
    Optional<MedicalReport> findByIdAndUserId(String id, Long userId);
}
