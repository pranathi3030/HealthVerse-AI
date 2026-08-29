package com.healthverse.report.controller;

import com.healthverse.report.dto.ReportUploadResponse;
import com.healthverse.report.exception.BadRequestException;
import com.healthverse.report.service.ReportService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
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

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportUploadResponse> uploadReport(
            Authentication authentication,
            @RequestParam("file") MultipartFile file,
            @RequestParam("reportType") String reportType,
            @RequestParam("reportDate") String reportDateStr,
            @RequestParam("description") String description) {
        
        Long userId = extractUserIdFromAuth(authentication);
        LocalDate reportDate = LocalDate.parse(reportDateStr);
        ReportUploadResponse response = reportService.uploadReport(userId, file, reportType, reportDate, description);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReportUploadResponse>> getAllReports(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        List<ReportUploadResponse> responses = reportService.getAllReportsForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportUploadResponse> getReportById(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = extractUserIdFromAuth(authentication);
        ReportUploadResponse response = reportService.getReportByIdForUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadReport(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = extractUserIdFromAuth(authentication);
        Map<String, Object> downloadData = reportService.downloadReportForUser(id, userId);
        
        Resource resource = (Resource) downloadData.get("resource");
        String contentType = (String) downloadData.get("contentType");
        String fileName = (String) downloadData.get("fileName");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = extractUserIdFromAuth(authentication);
        reportService.deleteReportForUser(id, userId);
        return ResponseEntity.noContent().build();
    }
}
