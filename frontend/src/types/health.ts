export interface HealthProfile {
  id?: string;
  age: number | '';
  gender: 'Male' | 'Female' | 'Other' | 'Prefer not to say' | '';
  height: number | ''; // in cm
  weight: number | ''; // in kg
  bloodGroup: 'A+' | 'A-' | 'B+' | 'B-' | 'O+' | 'O-' | 'AB+' | 'AB-' | '';
  allergies: string;
  chronicConditions: string;
  currentMedications: string;
  lifestyle: 'Sedentary' | 'Lightly Active' | 'Moderately Active' | 'Very Active' | 'Super Active' | '';
  fitnessGoal: 'Weight Loss' | 'Maintenance' | 'Muscle Gain' | 'Endurance' | '';
  sleepPattern: 'Less than 5 hours' | '5-7 hours' | '7-9 hours' | 'More than 9 hours' | '';
  dietaryPreference: 'None' | 'Vegetarian' | 'Vegan' | 'Keto' | 'Paleo' | 'Mediterranean' | '';
}

export const defaultHealthProfile: HealthProfile = {
  age: '',
  gender: '',
  height: '',
  weight: '',
  bloodGroup: '',
  allergies: '',
  chronicConditions: '',
  currentMedications: '',
  lifestyle: '',
  fitnessGoal: '',
  sleepPattern: '',
  dietaryPreference: '',
};
