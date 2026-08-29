package com.healthverse.aianalysis.entity;

import com.healthverse.aianalysis.enums.AnalysisStatus;
import com.healthverse.aianalysis.enums.ProcessingStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "medical_reports")
public class MedicalReport {
    @Id
    private String id;
    private Long userId;
    private String fileName;
    private String contentType;
    private ProcessingStatus processingStatus;
    private AnalysisStatus analysisStatus;
    private String gridFsFileId;
}
