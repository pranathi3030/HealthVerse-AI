package com.healthverse.aianalysis.dto;

import java.util.List;

public class HealthAnalyzeResponse {
    private String severity;
    private List<String> insights;
    private List<String> recommendations;
    private boolean seekProfessionalCare;

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public List<String> getInsights() { return insights; }
    public void setInsights(List<String> insights) { this.insights = insights; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    public boolean isSeekProfessionalCare() { return seekProfessionalCare; }
    public void setSeekProfessionalCare(boolean seekProfessionalCare) { this.seekProfessionalCare = seekProfessionalCare; }
}
