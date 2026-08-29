package com.healthverse.report.service;

import com.healthverse.report.dto.ReportUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
    ReportUploadResponse uploadReport(Long userId, MultipartFile file, String reportType, LocalDate reportDate, String description);
    List<ReportUploadResponse> getAllReportsForUser(Long userId);
    ReportUploadResponse getReportByIdForUser(String id, Long userId);
    Map<String, Object> downloadReportForUser(String id, Long userId);
    void deleteReportForUser(String id, Long userId);
}
