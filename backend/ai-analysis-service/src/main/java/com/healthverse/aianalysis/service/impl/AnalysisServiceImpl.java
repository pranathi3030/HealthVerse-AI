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
import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.healthverse.aianalysis.dto.ReportAnalyzeRequest;
import com.healthverse.aianalysis.dto.ReportAnalyzeResponse;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisReportRepository analysisRepository;
    private final MedicalReportRepository medicalReportRepository;
    private final PdfExtractionService pdfExtractionService;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;
    private final RestTemplate restTemplate;

    @Value("${ai.api-key:}")
    private String aiApiKey;

    public AnalysisServiceImpl(AnalysisReportRepository analysisRepository,
                               MedicalReportRepository medicalReportRepository,
                               PdfExtractionService pdfExtractionService,
                               GridFsTemplate gridFsTemplate,
                               GridFsOperations gridFsOperations,
                               RestTemplate restTemplate) {
        this.analysisRepository = analysisRepository;
        this.medicalReportRepository = medicalReportRepository;
        this.pdfExtractionService = pdfExtractionService;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
        this.restTemplate = restTemplate;
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

        AnalysisReport analysis = callExternalAiAnalysis(report, extractedText);
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

    private AnalysisReport callExternalAiAnalysis(MedicalReport report, String extractedText) {
        AnalysisReport analysis = new AnalysisReport();
        analysis.setReportId(report.getId());
        analysis.setUserId(report.getUserId());
        analysis.setCreatedAt(LocalDateTime.now());
        analysis.setUpdatedAt(LocalDateTime.now());
        analysis.setAnalysisStatus(AnalysisStatus.COMPLETED);
        analysis.setDisclaimer("This analysis is for informational purposes only and is not a medical diagnosis. Consult a qualified healthcare professional for medical advice.");
        
        try {
            String aiAgentUrl = System.getenv("AI_AGENT_URL");
            if (aiAgentUrl == null || aiAgentUrl.isBlank()) {
                aiAgentUrl = "http://localhost:8000";
            }
            ReportAnalyzeRequest request = new ReportAnalyzeRequest(extractedText);
            ResponseEntity<ReportAnalyzeResponse> response = restTemplate.postForEntity(
                aiAgentUrl + "/api/v1/agents/report/analyze", 
                request, 
                ReportAnalyzeResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ReportAnalyzeResponse aiResponse = response.getBody();
                analysis.setSummary(aiResponse.getSummary());
                
                List<AnalysisReport.Finding> findings = new ArrayList<>();
                if (aiResponse.getExtracted_values() != null) {
                    for (Map.Entry<String, String> entry : aiResponse.getExtracted_values().entrySet()) {
                        findings.add(new AnalysisReport.Finding(entry.getKey(), entry.getValue(), "N/A", "N/A", "Extracted"));
                    }
                }
                analysis.setFindings(findings);
                analysis.setAbnormalValues(aiResponse.getAbnormal_values());
                analysis.setRecommendations(aiResponse.getWellness_recommendations());
            } else {
                analysis.setSummary("Failed to get a successful response from AI Agent.");
            }
        } catch (Exception e) {
            analysis.setSummary("AI Service communication failed: " + e.getMessage());
            analysis.setFindings(new ArrayList<>());
            analysis.setAbnormalValues(new ArrayList<>());
            analysis.setRecommendations(new ArrayList<>());
        }
        
        return analysis;
    }
}
