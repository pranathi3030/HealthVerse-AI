package com.healthverse.aianalysis.dto;

import java.util.List;

public class HealthAnalyzeRequest {
    private String symptoms;
    private Integer age;
    private Double weight;
    private Double height;
    private List<String> allergies;
    private List<String> chronic_conditions;

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public List<String> getAllergies() { return allergies; }
    public void setAllergies(List<String> allergies) { this.allergies = allergies; }

    public List<String> getChronic_conditions() { return chronic_conditions; }
    public void setChronic_conditions(List<String> chronic_conditions) { this.chronic_conditions = chronic_conditions; }
}
