package com.healthverse.aianalysis.controller;

import com.healthverse.aianalysis.entity.AnalysisReport;
import com.healthverse.aianalysis.exception.BadRequestException;
import com.healthverse.aianalysis.service.AnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
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

    @PostMapping("/reports/{reportId}/analyze")
    public ResponseEntity<AnalysisReport> analyzeReport(
            Authentication authentication,
            @PathVariable String reportId) {
        Long userId = extractUserIdFromAuth(authentication);
        AnalysisReport response = analysisService.analyzeReport(reportId, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/reports/{reportId}/reanalyze")
    public ResponseEntity<AnalysisReport> reanalyzeReport(
            Authentication authentication,
            @PathVariable String reportId) {
        Long userId = extractUserIdFromAuth(authentication);
        AnalysisReport response = analysisService.reanalyzeReport(reportId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<AnalysisReport> getAnalysisByReportId(
            Authentication authentication,
            @PathVariable String reportId) {
        Long userId = extractUserIdFromAuth(authentication);
        AnalysisReport response = analysisService.getAnalysisByReportId(reportId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisReport> getAnalysisById(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = extractUserIdFromAuth(authentication);
        AnalysisReport response = analysisService.getAnalysisById(id, userId);
        return ResponseEntity.ok(response);
    }
}
