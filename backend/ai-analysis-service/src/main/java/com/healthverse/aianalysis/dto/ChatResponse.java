package com.healthverse.aianalysis.dto;

public class ChatResponse {
    private String response;
    private String disclaimer;
    private boolean recommend_professional;

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }

    public boolean isRecommend_professional() { return recommend_professional; }
    public void setRecommend_professional(boolean recommend_professional) { this.recommend_professional = recommend_professional; }
}
