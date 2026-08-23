package com.healthverse.report.repository;

import com.healthverse.report.entity.MedicalReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalReportRepository extends MongoRepository<MedicalReport, String> {
    List<MedicalReport> findByUserIdOrderByUploadDateDesc(Long userId);
    Optional<MedicalReport> findByIdAndUserId(String id, Long userId);
}
