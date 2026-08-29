package com.healthverse.aianalysis.service;

import com.healthverse.aianalysis.entity.AnalysisReport;

public interface AnalysisService {
    AnalysisReport analyzeReport(String reportId, Long userId);
    AnalysisReport getAnalysisByReportId(String reportId, Long userId);
    AnalysisReport getAnalysisById(String analysisId, Long userId);
    AnalysisReport reanalyzeReport(String reportId, Long userId);
}
