package com.healthverse.nutritionfitness.dto;

import java.util.List;

public class FitnessGenerateResponse {
    private List<String> exercise_plan;
    private List<String> recovery_suggestions;
    private List<String> general_advice;

    public List<String> getExercise_plan() { return exercise_plan; }
    public void setExercise_plan(List<String> exercise_plan) { this.exercise_plan = exercise_plan; }

    public List<String> getRecovery_suggestions() { return recovery_suggestions; }
    public void setRecovery_suggestions(List<String> recovery_suggestions) { this.recovery_suggestions = recovery_suggestions; }

    public List<String> getGeneral_advice() { return general_advice; }
    public void setGeneral_advice(List<String> general_advice) { this.general_advice = general_advice; }
}
