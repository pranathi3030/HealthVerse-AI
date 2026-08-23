package com.healthverse.aianalysis.service.impl;

import com.healthverse.aianalysis.entity.AnalysisReport;
import com.healthverse.aianalysis.entity.MedicalReport;
import com.healthverse.aianalysis.enums.AnalysisStatus;
import com.healthverse.aianalysis.exception.BadRequestException;
import com.healthverse.aianalysis.exception.ResourceNotFoundException;
import com.healthverse.aianalysis.repository.AnalysisReportRepository;
import com.healthverse.aianalysis.repository.MedicalReportRepository;
import com.healthverse.aianalysis.service.AnalysisService;
import com.healthverse.aianalysis.service.PdfExtractionService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisReportRepository analysisRepository;
    private final MedicalReportRepository medicalReportRepository;
    private final PdfExtractionService pdfExtractionService;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    @Value("${ai.api-key:}")
    private String aiApiKey;

    public AnalysisServiceImpl(AnalysisReportRepository analysisRepository,
                               MedicalReportRepository medicalReportRepository,
                               PdfExtractionService pdfExtractionService,
                               GridFsTemplate gridFsTemplate,
                               GridFsOperations gridFsOperations) {
        this.analysisRepository = analysisRepository;
        this.medicalReportRepository = medicalReportRepository;
        this.pdfExtractionService = pdfExtractionService;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    @Override
    public AnalysisReport analyzeReport(String reportId, Long userId) {
        // Ensure no duplicate analysis exists
        Optional<AnalysisReport> existing = analysisRepository.findByReportIdAndUserId(reportId, userId);
        if (existing.isPresent()) {
            throw new BadRequestException("Analysis already exists for this report. Use reanalyze instead.");
        }
        
        return processAnalysis(reportId, userId);
    }

    @Override
    public AnalysisReport reanalyzeReport(String reportId, Long userId) {
        AnalysisReport existing = analysisRepository.findByReportIdAndUserId(reportId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("No existing analysis found to reanalyze"));
        
        analysisRepository.delete(existing);
        return processAnalysis(reportId, userId);
    }

    @Override
    public AnalysisReport getAnalysisByReportId(String reportId, Long userId) {
        return analysisRepository.findByReportIdAndUserId(reportId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found for this report"));
    }

    @Override
    public AnalysisReport getAnalysisById(String analysisId, Long userId) {
        return analysisRepository.findByIdAndUserId(analysisId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found"));
    }

    private AnalysisReport processAnalysis(String reportId, Long userId) {
        MedicalReport report = medicalReportRepository.findByIdAndUserId(reportId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical report not found or unauthorized"));

        String extractedText = "No text extracted.";
        
        if ("application/pdf".equals(report.getContentType())) {
            extractedText = extractFromGridFs(report.getGridFsFileId());
        } else {
            extractedText = "[Image format - OCR not implemented in mock mode]";
        }

        AnalysisReport analysis = performMockAiAnalysis(report, extractedText);
        return analysisRepository.save(analysis);
    }

    private String extractFromGridFs(String gridFsFileId) {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(gridFsFileId)));
            if (gridFSFile != null) {
                GridFsResource resource = gridFsOperations.getResource(gridFSFile);
                try (InputStream is = resource.getInputStream()) {
                    return pdfExtractionService.extractTextFromPdf(is);
                }
            }
        } catch (Exception e) {
            return "Error extracting text: " + e.getMessage();
        }
        return "File not found in GridFS";
    }

    private AnalysisReport performMockAiAnalysis(MedicalReport report, String extractedText) {
        AnalysisReport analysis = new AnalysisReport();
        analysis.setReportId(report.getId());
        analysis.setUserId(report.getUserId());
        analysis.setCreatedAt(LocalDateTime.now());
        analysis.setUpdatedAt(LocalDateTime.now());
        analysis.setAnalysisStatus(AnalysisStatus.COMPLETED);
        
        analysis.setDisclaimer("This analysis is for informational purposes only and is not a medical diagnosis. Consult a qualified healthcare professional for medical advice.");
        
        List<AnalysisReport.Finding> findings = new ArrayList<>();
        List<String> abnormals = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        
        if (extractedText.toLowerCase().contains("cholesterol")) {
            findings.add(new AnalysisReport.Finding("Cholesterol", "210", "mg/dL", "< 200", "Borderline High"));
            abnormals.add("Elevated Cholesterol detected.");
            recommendations.add("Consider a low-cholesterol diet and regular exercise.");
        }
        
        if (extractedText.toLowerCase().contains("glucose")) {
            findings.add(new AnalysisReport.Finding("Glucose (Fasting)", "105", "mg/dL", "70-99", "Impaired Fasting Glucose"));
            abnormals.add("Slightly elevated Fasting Glucose.");
            recommendations.add("Monitor blood sugar levels and reduce simple carbohydrate intake.");
        }
        
        if (findings.isEmpty()) {
            analysis.setSummary("General mock analysis completed. No specific targeted parameters (like Cholesterol or Glucose) were detected in the text.");
            findings.add(new AnalysisReport.Finding("General Parameter", "Normal", "N/A", "N/A", "Within normal limits"));
            recommendations.add("Continue regular annual checkups.");
        } else {
            analysis.setSummary("Mock AI Analysis completed successfully. Certain parameters were flagged as outside the normal reference ranges.");
        }

        if (aiApiKey != null && !aiApiKey.isEmpty()) {
            analysis.setSummary("[LIVE API MODE NOT FULLY IMPLEMENTED] " + analysis.getSummary());
        } else {
            analysis.setSummary("[MOCK/DEMO MODE] " + analysis.getSummary());
        }

        analysis.setFindings(findings);
        analysis.setAbnormalValues(abnormals);
        analysis.setRecommendations(recommendations);
        
        return analysis;
    }
}
