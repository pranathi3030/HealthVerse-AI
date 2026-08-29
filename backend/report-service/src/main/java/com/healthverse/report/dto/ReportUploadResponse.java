package com.healthverse.report.dto;

import com.healthverse.report.enums.AnalysisStatus;
import com.healthverse.report.enums.ProcessingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportUploadResponse {
    private String id;
    private Long userId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String reportType;
    private LocalDate reportDate;
    private String description;
    private LocalDateTime uploadDate;
    private ProcessingStatus processingStatus;
    private AnalysisStatus analysisStatus;
}
