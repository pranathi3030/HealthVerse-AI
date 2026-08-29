package com.healthverse.aianalysis.dto;

public class ReportAnalyzeRequest {
    private String report_text;

    public ReportAnalyzeRequest() {}

    public ReportAnalyzeRequest(String report_text) {
        this.report_text = report_text;
    }

    public String getReport_text() {
        return report_text;
    }

    public void setReport_text(String report_text) {
        this.report_text = report_text;
    }
}
