package com.healthverse.wellness.dto;

public class WellnessGenerateRequest {
    private String sleep_quality;
    private String stress_level;
    private String daily_activity;

    public String getSleep_quality() { return sleep_quality; }
    public void setSleep_quality(String sleep_quality) { this.sleep_quality = sleep_quality; }

    public String getStress_level() { return stress_level; }
    public void setStress_level(String stress_level) { this.stress_level = stress_level; }

    public String getDaily_activity() { return daily_activity; }
    public void setDaily_activity(String daily_activity) { this.daily_activity = daily_activity; }
}
