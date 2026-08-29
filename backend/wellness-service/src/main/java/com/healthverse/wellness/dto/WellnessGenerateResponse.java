package com.healthverse.wellness.dto;

import java.util.List;

public class WellnessGenerateResponse {
    private List<String> sleep_recommendations;
    private List<String> stress_management;
    private List<String> daily_habits;

    public List<String> getSleep_recommendations() { return sleep_recommendations; }
    public void setSleep_recommendations(List<String> sleep_recommendations) { this.sleep_recommendations = sleep_recommendations; }

    public List<String> getStress_management() { return stress_management; }
    public void setStress_management(List<String> stress_management) { this.stress_management = stress_management; }

    public List<String> getDaily_habits() { return daily_habits; }
    public void setDaily_habits(List<String> daily_habits) { this.daily_habits = daily_habits; }
}
