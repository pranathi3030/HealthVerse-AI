package com.healthverse.aianalysis.repository;

import com.healthverse.aianalysis.entity.AnalysisReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnalysisReportRepository extends MongoRepository<AnalysisReport, String> {
    Optional<AnalysisReport> findByReportIdAndUserId(String reportId, Long userId);
    Optional<AnalysisReport> findByIdAndUserId(String id, Long userId);
}
