package com.healthverse.aianalysis.dto;

import java.util.List;
import java.util.Map;

public class ReportAnalyzeResponse {
    private String summary;
    private Map<String, String> extracted_values;
    private List<String> abnormal_values;
    private List<String> wellness_recommendations;
    private boolean seekProfessionalCare;

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Map<String, String> getExtracted_values() { return extracted_values; }
    public void setExtracted_values(Map<String, String> extracted_values) { this.extracted_values = extracted_values; }

    public List<String> getAbnormal_values() { return abnormal_values; }
    public void setAbnormal_values(List<String> abnormal_values) { this.abnormal_values = abnormal_values; }

    public List<String> getWellness_recommendations() { return wellness_recommendations; }
    public void setWellness_recommendations(List<String> wellness_recommendations) { this.wellness_recommendations = wellness_recommendations; }

    public boolean isSeekProfessionalCare() { return seekProfessionalCare; }
    public void setSeekProfessionalCare(boolean seekProfessionalCare) { this.seekProfessionalCare = seekProfessionalCare; }
}
