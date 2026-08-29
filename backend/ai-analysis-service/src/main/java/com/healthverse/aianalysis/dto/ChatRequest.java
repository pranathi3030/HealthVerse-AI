package com.healthverse.aianalysis.dto;

import java.util.Map;

public class ChatRequest {
    private String query;
    private Map<String, Object> context;

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public Map<String, Object> getContext() { return context; }
    public void setContext(Map<String, Object> context) { this.context = context; }
}
