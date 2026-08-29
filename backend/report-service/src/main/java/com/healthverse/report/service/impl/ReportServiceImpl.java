package com.healthverse.report.service.impl;

import com.healthverse.report.dto.ReportUploadResponse;
import com.healthverse.report.entity.MedicalReport;
import com.healthverse.report.enums.AnalysisStatus;
import com.healthverse.report.enums.ProcessingStatus;
import com.healthverse.report.exception.BadRequestException;
import com.healthverse.report.exception.ResourceNotFoundException;
import com.healthverse.report.repository.MedicalReportRepository;
import com.healthverse.report.service.ReportService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final MedicalReportRepository repository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    public ReportServiceImpl(MedicalReportRepository repository, GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
        this.repository = repository;
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    @Override
    public ReportUploadResponse uploadReport(Long userId, MultipartFile file, String reportType, LocalDate reportDate, String description) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        
        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Unsupported file type. Only PDF, JPG, and PNG are allowed.");
        }

        try {
            ObjectId gridFsId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            
            MedicalReport report = MedicalReport.builder()
                    .userId(userId)
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .reportType(reportType)
                    .reportDate(reportDate)
                    .description(description)
                    .uploadDate(LocalDateTime.now())
                    .processingStatus(ProcessingStatus.UPLOADED)
                    .analysisStatus(AnalysisStatus.PENDING)
                    .gridFsFileId(gridFsId.toString())
                    .build();

            MedicalReport saved = repository.save(report);
            return mapToResponse(saved);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public List<ReportUploadResponse> getAllReportsForUser(Long userId) {
        return repository.findByUserIdOrderByUploadDateDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReportUploadResponse getReportByIdForUser(String id, Long userId) {
        MedicalReport report = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        return mapToResponse(report);
    }

    @Override
    public Map<String, Object> downloadReportForUser(String id, Long userId) {
        MedicalReport report = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(report.getGridFsFileId())));
            if (gridFSFile == null) {
                throw new ResourceNotFoundException("File content not found in GridFS");
            }
            GridFsResource resource = gridFsOperations.getResource(gridFSFile);
            
            Map<String, Object> result = new HashMap<>();
            result.put("resource", new InputStreamResource(resource.getInputStream()));
            result.put("contentType", report.getContentType());
            result.put("fileName", report.getFileName());
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @Override
    public void deleteReportForUser(String id, Long userId) {
        MedicalReport report = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));
        
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(report.getGridFsFileId())));
        repository.delete(report);
    }

    private ReportUploadResponse mapToResponse(MedicalReport entity) {
        return ReportUploadResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .fileName(entity.getFileName())
                .contentType(entity.getContentType())
                .fileSize(entity.getFileSize())
                .reportType(entity.getReportType())
                .reportDate(entity.getReportDate())
                .description(entity.getDescription())
                .uploadDate(entity.getUploadDate())
                .processingStatus(entity.getProcessingStatus())
                .analysisStatus(entity.getAnalysisStatus())
                .build();
    }
}
