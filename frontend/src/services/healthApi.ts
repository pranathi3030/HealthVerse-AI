import api from './api';
import { HealthProfile } from '../types/health';

const USE_MOCK = false;
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

let mockProfile: HealthProfile = {
  age: 30,
  gender: 'Male',
  height: 175,
  weight: 72,
  bloodGroup: 'O+',
  allergies: 'None',
  chronicConditions: 'None',
  currentMedications: 'Vitamin D3',
  lifestyle: 'Moderately Active',
  fitnessGoal: 'Maintenance',
  sleepPattern: '7-9 hours',
  dietaryPreference: 'None',
};

export const healthApi = {
  getProfile: async (userId: string): Promise<HealthProfile> => {
    if (USE_MOCK) {
      await delay(600);
      return { ...mockProfile };
    }
    const response = await api.get<any>(`/api/health/profile/${userId}`);
    const data = response.data;
    return {
      age: data.age || 0,
      gender: data.gender || '',
      height: data.height || 0,
      weight: data.weight || 0,
      lifestyle: data.lifestyle || '',
      fitnessGoal: data.goals || '',
      allergies: data.allergies || '',
      chronicConditions: data.conditions || '',
      // Unsupported backend fields default to empty strings
      bloodGroup: '',
      currentMedications: '',
      sleepPattern: '',
      dietaryPreference: ''
    };
  },

  updateProfile: async (profile: HealthProfile): Promise<HealthProfile> => {
    if (USE_MOCK) {
      await delay(800);
      mockProfile = { ...profile };
      return { ...mockProfile };
    }
    // Map to backend UpdateHealthProfileRequest contract
    const payload = {
      age: profile.age,
      gender: profile.gender,
      height: profile.height,
      weight: profile.weight,
      lifestyle: profile.lifestyle,
      goals: profile.fitnessGoal,
      allergies: profile.allergies,
      conditions: profile.chronicConditions
    };
    const response = await api.put<any>('/api/health/profile', payload);
    const data = response.data;
    return {
      ...profile, // Keep unsupported fields locally
      age: data.age,
      gender: data.gender,
      height: data.height,
      weight: data.weight,
      lifestyle: data.lifestyle,
      fitnessGoal: data.goals,
      allergies: data.allergies,
      chronicConditions: data.conditions
    };
  }
};
