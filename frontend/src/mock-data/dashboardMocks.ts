export interface Metric {
  label: string;
  value: string | number;
  unit?: string;
  trend?: 'up' | 'down' | 'stable';
  trendValue?: string;
  status?: 'good' | 'warning' | 'critical';
}

export interface ChartDataPoint {
  name: string;
  weight?: number;
  sleep?: number;
  activity?: number;
}

export interface Report {
  id: string;
  date: string;
  title: string;
  status: 'analyzed' | 'processing';
  summary?: string;
}

export interface WellnessPlanItem {
  id: string;
  time: string;
  activity: string;
  completed: boolean;
}

export interface MedicationReminder {
  id: string;
  name: string;
  time: string;
  status: 'taken' | 'scheduled' | 'missed';
}

export const mockMetrics = {
  steps: { label: 'Daily Steps', value: 6432, unit: 'steps', trend: 'up', trendValue: '+12%', status: 'good' } as Metric,
  sleep: { label: 'Sleep', value: 7.2, unit: 'hrs', trend: 'down', trendValue: '-30m', status: 'warning' } as Metric,
  calories: { label: 'Calories', value: 1840, unit: 'kcal', trend: 'stable', trendValue: '0%', status: 'good' } as Metric,
  hydration: { label: 'Hydration', value: 1.2, unit: 'L', trend: 'down', trendValue: '-0.5L', status: 'warning' } as Metric,
  bmi: { label: 'BMI', value: 24.5, trend: 'stable', status: 'good' } as Metric,
  weight: { label: 'Weight', value: 72, unit: 'kg', trend: 'down', trendValue: '-0.5kg', status: 'good' } as Metric,
};

export const mockTrendData: ChartDataPoint[] = [
  { name: 'Mon', weight: 73.0, sleep: 6.5, activity: 5000 },
  { name: 'Tue', weight: 72.8, sleep: 7.0, activity: 7000 },
  { name: 'Wed', weight: 72.9, sleep: 6.8, activity: 6500 },
  { name: 'Thu', weight: 72.5, sleep: 7.5, activity: 8000 },
  { name: 'Fri', weight: 72.3, sleep: 7.2, activity: 8500 },
  { name: 'Sat', weight: 72.1, sleep: 8.0, activity: 10000 },
  { name: 'Sun', weight: 72.0, sleep: 7.8, activity: 9000 },
];

export const mockRecentReports: Report[] = [
  { id: '1', date: '2026-08-15', title: 'Comprehensive Blood Test', status: 'analyzed', summary: 'Vitamin D is slightly low.' },
  { id: '2', date: '2026-08-20', title: 'MRI Scan - Knee', status: 'processing' }
];

export const mockWellnessPlan: WellnessPlanItem[] = [
  { id: '1', time: '08:00 AM', activity: 'Morning Stretching (15 min)', completed: true },
  { id: '2', time: '01:00 PM', activity: 'Drink 500ml Water', completed: false },
  { id: '3', time: '06:00 PM', activity: 'Evening Walk (30 min)', completed: false },
];

export const mockMedicationReminders: MedicationReminder[] = [
  { id: '1', name: 'Vitamin D3', time: '08:00 AM', status: 'taken' },
  { id: '2', name: 'Omega 3', time: '08:00 PM', status: 'scheduled' },
];

export const mockWellnessIndicators = [
  { label: 'Cardiovascular Health', score: 85, max: 100 },
  { label: 'Recovery & Sleep', score: 72, max: 100 },
  { label: 'Nutrition & Diet', score: 90, max: 100 },
];
