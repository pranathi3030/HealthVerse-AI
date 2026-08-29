import api from './api';
import { TwinMetrics } from '../types/twin';

const USE_MOCK = false;
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const twinApi = {
  getTwinMetrics: async (): Promise<TwinMetrics> => {
    if (USE_MOCK) {
      await delay(1200);
      return {
        weightTrend: [
          { date: 'Mon', weight: 73.5, bmi: 25.0 },
          { date: 'Tue', weight: 73.2, bmi: 24.9 },
          { date: 'Wed', weight: 73.0, bmi: 24.8 },
          { date: 'Thu', weight: 72.8, bmi: 24.7 },
          { date: 'Fri', weight: 72.5, bmi: 24.6 },
          { date: 'Sat', weight: 72.2, bmi: 24.5 },
          { date: 'Sun', weight: 72.0, bmi: 24.5 },
        ],
        activitySleepTrend: [
          { date: 'Mon', activity: 4000, sleep: 5.5 },
          { date: 'Tue', activity: 5000, sleep: 6.0 },
          { date: 'Wed', activity: 5500, sleep: 6.5 },
          { date: 'Thu', activity: 7000, sleep: 7.0 },
          { date: 'Fri', activity: 8000, sleep: 7.5 },
          { date: 'Sat', activity: 10000, sleep: 8.0 },
          { date: 'Sun', activity: 9000, sleep: 8.5 },
        ],
        nutritionAdherence: 85,
        medicationAdherence: 95,
        overallWellnessScore: 82,
        status: 'improving',
        radarData: [
          { subject: 'Physical', A: 85, fullMark: 100 },
          { subject: 'Mental', A: 90, fullMark: 100 },
          { subject: 'Recovery', A: 75, fullMark: 100 },
          { subject: 'Sleep', A: 70, fullMark: 100 },
          { subject: 'Nutrition', A: 95, fullMark: 100 },
          { subject: 'Focus', A: 88, fullMark: 100 },
        ],
        journeyEvents: [
          { date: 'Today', event: 'Nutrition Target Hit', type: 'positive' },
          { date: 'Yesterday', event: 'Sleep deficit detected', type: 'warning' },
          { date: 'Aug 18', event: 'Started new fitness plan', type: 'info' },
          { date: 'Aug 15', event: 'Blood report analyzed', type: 'success' },
        ]
      };
    }
    const response = await api.get<TwinMetrics>('/api/health-twin/status');
    return response.data;
  }
};
