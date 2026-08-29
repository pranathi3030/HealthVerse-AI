import api from './api';
import { NutritionPlan, FitnessPlan, WellnessPlan } from '../types/wellness';

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const wellnessApi = {
  getNutritionPlan: async (): Promise<NutritionPlan> => {
    try {
      const res = await api.get<any[]>('/api/nutrition/plans');
      let data = res.data && res.data.length > 0 ? res.data[0] : null;
      
      if (!data) {
        try {
          const genRes = await api.post<any>('/api/nutrition/plans/generate', {});
          data = genRes.data;
        } catch (genError) {
          console.warn("Failed to generate nutrition plan on backend, using fallback data", genError);
          data = {
            dailyCalories: 2000,
            proteinGrams: 100,
            waterLiters: 2.5,
            mealPlan: 'Breakfast: Oatmeal. Lunch: Salad. Dinner: Chicken.'
          };
        }
      }
      
      return {
        dailyCaloriesTarget: data.dailyCalories || 2200,
        proteinTarget: data.proteinGrams || 120,
        waterTarget: data.waterLiters || 2.5,
        meals: {
          breakfast: data.mealPlan ? [data.mealPlan] : ['Oatmeal with berries', '2 boiled eggs', 'Green tea'],
          lunch: ['Grilled chicken salad', 'Quinoa', 'Olive oil dressing'],
          dinner: ['Baked salmon', 'Steamed broccoli', 'Sweet potato'],
          snacks: ['Greek yogurt', 'Almonds', 'Apple'],
        }
      };
    } catch (e) {
      console.error(e);
      throw e;
    }
  },

  getFitnessPlan: async (): Promise<FitnessPlan> => {
    try {
      const res = await api.get<any[]>('/api/fitness/plans');
      let data = res.data && res.data.length > 0 ? res.data[0] : null;
      
      if (!data) {
        try {
          const genRes = await api.post<any>('/api/fitness/plans/generate', {});
          data = genRes.data;
        } catch (genError) {
          console.warn("Failed to generate fitness plan on backend, using fallback data", genError);
          data = {
            sessionDurationMinutes: 30,
            workoutPlan: '30m Jogging'
          };
        }
      }
      
      return {
        dailyStepGoal: 10000, // Backend doesn't support this
        workoutDuration: data.sessionDurationMinutes || 45,
        weeklyPlan: data.workoutPlan ? 
          [{ day: 'Everyday', activity: data.workoutPlan }] : 
          [
            { day: 'Monday', activity: '30m Jogging + 15m Core' },
            { day: 'Tuesday', activity: '45m Weight Training (Upper Body)' },
            { day: 'Wednesday', activity: 'Active Recovery / Yoga' },
            { day: 'Thursday', activity: '45m Weight Training (Lower Body)' },
            { day: 'Friday', activity: 'HIIT Cardio' },
            { day: 'Saturday', activity: 'Long Walk / Hiking' },
            { day: 'Sunday', activity: 'Rest' },
          ]
      };
    } catch (e) {
      console.error(e);
      throw e;
    }
  },

  getWellnessPlan: async (): Promise<WellnessPlan> => {
    try {
      const res = await api.get<any[]>('/api/wellness/plans');
      let data = res.data && res.data.length > 0 ? res.data[0] : null;
      
      if (!data) {
        try {
          const genRes = await api.post<any>('/api/wellness/plans/generate', {});
          data = genRes.data;
        } catch (genError) {
          console.warn("Failed to generate wellness plan on backend, using fallback data", genError);
          data = {
            description: 'Maintain a consistent sleep schedule.'
          };
        }
      }
      
      return {
        sleepGoal: 8,
        currentStressLevel: 'Moderate',
        recommendations: data.description ? [data.description] : [
          'Maintain a consistent sleep schedule by going to bed at the same time.',
          'Limit screen time 1 hour before sleeping.',
          'Practice gratitude journaling in the morning.'
        ],
        breathingExercises: [
          '4-7-8 Breathing Technique: Inhale for 4s, hold for 7s, exhale for 8s.',
          'Box Breathing: Inhale 4s, hold 4s, exhale 4s, hold 4s.'
        ]
      };
    } catch (e) {
      console.error(e);
      throw e;
    }
  },

  getPredictiveInsights: async (): Promise<any> => {
    try {
      const res = await api.get<any>('/api/wellness/insights');
      return res.data;
    } catch (e) {
      console.error(e);
      throw e;
    }
  }
};
