import api from './api';
import { SymptomPayload, SymptomAnalysisResponse, ChatPayload, ChatResponse } from '../types/ai';

const USE_MOCK = false;
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const aiApi = {
  analyzeSymptoms: async (payload: SymptomPayload): Promise<SymptomAnalysisResponse> => {
    if (USE_MOCK) {
      await delay(2000);
      return {
        insights: `Based on your reported symptoms ("${payload.symptoms}"), it appears you may be experiencing a common viral infection or seasonal allergies, especially considering the ${payload.severity?.toLowerCase() || 'reported'} severity.`,
        recommendations: [
          'Ensure you are getting at least 8 hours of sleep.',
          'Stay hydrated by drinking plenty of water and clear fluids.',
          'Consider over-the-counter remedies if symptoms persist.',
          'Rest and monitor your temperature.'
        ],
        seekProfessionalCare: payload.severity === 'Severe',
      };
    }
    const response = await api.post<SymptomAnalysisResponse>('/api/analysis/symptoms', payload);
    return response.data;
  },

  chat: async (payload: ChatPayload): Promise<ChatResponse> => {
    if (USE_MOCK) {
      await delay(1500);
      return {
        message: `This is a mock AI response to: "${payload.message}". Remember to stay hydrated and maintain a balanced diet!`,
        disclaimer: "Mock Multi-Agent Collaboration",
      };
    }
    const response = await api.post<any>('/api/analysis/chat', payload);
    return {
      message: response.data.response || response.data.message || "No response received",
      disclaimer: response.data.disclaimer
    };
  }
};
