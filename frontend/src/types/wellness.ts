export interface NutritionPlan {
  dailyCaloriesTarget: number;
  proteinTarget: number;
  waterTarget: number;
  meals: {
    breakfast: string[];
    lunch: string[];
    dinner: string[];
    snacks: string[];
  };
}

export interface FitnessPlan {
  dailyStepGoal: number;
  workoutDuration: number; // in minutes
  weeklyPlan: {
    day: string;
    activity: string;
  }[];
}

export interface WellnessPlan {
  sleepGoal: number;
  currentStressLevel: 'Low' | 'Moderate' | 'High';
  recommendations: string[];
  breathingExercises: string[];
}

export interface PredictiveInsight {
  area: string;
  trend: string;
  reason: string;
  recommendation: string;
  confidence: string;
  contributing_factors: string[];
  evidence: string[];
}
