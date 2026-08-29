export interface TwinMetrics {
  weightTrend: { date: string; weight: number; bmi: number }[];
  activitySleepTrend: { date: string; activity: number; sleep: number }[];
  nutritionAdherence: number; // percentage 0-100
  medicationAdherence: number; // percentage 0-100
  overallWellnessScore: number; // percentage 0-100
  status: 'optimal' | 'improving' | 'needs_attention' | 'insufficient data';
  radarData: { subject: string; A: number; fullMark: number }[];
  journeyEvents: { date: string; event: string; type: 'positive' | 'warning' | 'info' | 'success' }[];
}
