import api from './api';
import { ReportAnalysis } from '../types/report';

const USE_MOCK = false;
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

const mockAnalysis: ReportAnalysis = {
  id: 'report_123',
  reportTitle: 'Comprehensive Blood Panel',
  dateAnalyzed: new Date().toISOString().split('T')[0],
  summary: 'Overall blood counts are within normal ranges. However, Vitamin D levels are notably low, and LDL cholesterol is slightly elevated.',
  simpleExplanation: 'Your blood test looks mostly healthy! The main things to notice are that you need more Vitamin D (which you get from sunlight or supplements) and you should watch your cholesterol by eating healthier fats.',
  observations: [
    { name: 'Hemoglobin', value: '14.2 g/dL', status: 'normal', description: 'Normal oxygen carrying capacity.' },
    { name: 'Vitamin D (25-OH)', value: '18 ng/mL', status: 'attention', description: 'Below the recommended 30 ng/mL. Indicates deficiency.' },
    { name: 'LDL Cholesterol', value: '135 mg/dL', status: 'attention', description: 'Slightly above the optimal range of < 100 mg/dL.' },
    { name: 'Fasting Glucose', value: '88 mg/dL', status: 'normal', description: 'Healthy blood sugar levels.' },
  ],
  recommendations: [
    'Start a daily Vitamin D3 supplement (consult your doctor for dosage).',
    'Increase exposure to morning sunlight for 15-20 minutes daily.',
    'Incorporate more omega-3 rich foods like salmon or walnuts into your diet to help manage LDL cholesterol.',
    'Maintain your current healthy sugar intake.'
  ],
  seekProfessionalCare: false,
};

export const reportApi = {
  uploadReport: async (file: File, onProgress?: (progress: number) => void): Promise<{ id: string }> => {
    if (USE_MOCK) {
      // Simulate upload progress
      if (onProgress) {
        for (let i = 10; i <= 100; i += 20) {
          await delay(300);
          onProgress(i);
        }
      } else {
        await delay(1500);
      }
      return { id: 'report_123' };
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('reportType', 'GENERAL_BLOOD_TEST');
    formData.append('reportDate', new Date().toISOString().split('T')[0]);
    formData.append('description', 'User uploaded report via UI');
    
    const response = await api.post<{ id: string }>('/api/reports/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress(percentCompleted);
        }
      }
    });
    return response.data;
  },

  getAnalysis: async (id: string): Promise<ReportAnalysis> => {
    if (USE_MOCK) {
      // Simulate complex AI processing time
      await delay(2500);
      return { ...mockAnalysis, id };
    }

    const response = await api.get<ReportAnalysis>(`/api/analysis/reports/${id}`);
    return response.data;
  }
};
