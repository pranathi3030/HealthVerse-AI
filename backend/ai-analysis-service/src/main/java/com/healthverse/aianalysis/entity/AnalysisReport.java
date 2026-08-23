package com.healthverse.aianalysis.entity;

import com.healthverse.aianalysis.enums.AnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analysis_reports")
public class AnalysisReport {

    @Id
    private String id;
    
    private String reportId;
    private Long userId;
    
    private AnalysisStatus analysisStatus;
    
    private String summary;
    
    private List<Finding> findings;
    private List<String> abnormalValues;
    private List<String> recommendations;
    
    private String disclaimer;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Finding {
        private String parameter;
        private String value;
        private String unit;
        private String referenceRange;
        private String interpretation;
    }
}
