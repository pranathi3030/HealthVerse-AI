package com.healthverse.nutritionfitness.dto;

import java.util.List;

public class DietGenerateRequest {
    private String goal;
    private String diet_type;
    private Integer age;
    private Double weight;
    private Double height;
    private List<String> allergies;
    private List<String> chronic_conditions;

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getDiet_type() { return diet_type; }
    public void setDiet_type(String diet_type) { this.diet_type = diet_type; }

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
