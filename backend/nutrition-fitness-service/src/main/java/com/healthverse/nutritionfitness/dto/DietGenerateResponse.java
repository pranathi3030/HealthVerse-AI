package com.healthverse.nutritionfitness.dto;

import java.util.List;

public class DietGenerateResponse {
    private List<String> meal_suggestions;
    private String hydration_guidance;
    private List<String> practical_recommendations;

    public List<String> getMeal_suggestions() { return meal_suggestions; }
    public void setMeal_suggestions(List<String> meal_suggestions) { this.meal_suggestions = meal_suggestions; }

    public String getHydration_guidance() { return hydration_guidance; }
    public void setHydration_guidance(String hydration_guidance) { this.hydration_guidance = hydration_guidance; }

    public List<String> getPractical_recommendations() { return practical_recommendations; }
    public void setPractical_recommendations(List<String> practical_recommendations) { this.practical_recommendations = practical_recommendations; }
}
