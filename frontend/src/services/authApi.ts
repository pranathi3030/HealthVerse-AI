import api from './api';
import { LoginPayload, RegisterPayload, AuthResponse } from '../types/auth';

// Use this toggle to switch between mock mode and real API mode
const USE_MOCK = false;

// Delay helper to simulate network request
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const authApi = {
  login: async (payload: LoginPayload): Promise<AuthResponse> => {
    if (USE_MOCK) {
      await delay(500);
      // Accept any email/password in mock mode
      return {
        accessToken: 'mock-jwt-token-123456789',
        user: {
          id: 'u_123',
          name: payload.email.split('@')[0].replace(/[._]/g, ' ').replace(/\b\w/g, c => c.toUpperCase()),
          email: payload.email,
        },
      };
    }

    // Real API integration
    const response = await api.post<AuthResponse>('/api/auth/login', payload);
    return response.data;
  },

  register: async (payload: RegisterPayload): Promise<AuthResponse> => {
    if (USE_MOCK) {
      await delay(500);
      return {
        accessToken: 'mock-jwt-token-987654321',
        user: {
          id: `u_${Date.now()}`,
          name: payload.name,
          email: payload.email,
        },
      };
    }

    // Real API integration
    const response = await api.post<AuthResponse>('/api/auth/register', payload);
    return response.data;
  },

  verifyToken: async (): Promise<AuthResponse> => {
    if (USE_MOCK) {
      await delay(300);
      return {
        accessToken: 'mock-jwt-token',
        user: {
          id: 'u_123',
          name: 'HealthVerse User',
          email: 'user@healthverse.ai',
        },
      };
    }

    const response = await api.get<{ id: number; name: string; email: string; role?: string }>('/users/profile');
    const profileData = response.data;
    return {
      accessToken: localStorage.getItem('token') || '',
      user: {
        id: String(profileData.id),
        name: profileData.name,
        email: profileData.email,
      },
    };
  }
};
