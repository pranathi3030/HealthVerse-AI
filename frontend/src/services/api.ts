import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const url = error.config?.url || '';
      // Only clear token for non-auth endpoints (don't clear during login/register attempts)
      const isAuthRequest = url.includes('/api/auth/');
      if (!isAuthRequest) {
        localStorage.removeItem('token');
        // Do NOT do window.location.href redirect here.
        // React's DashboardLayout route guard handles the redirect via <Navigate to="/login" />
      }
    }
    return Promise.reject(error);
  }
);

export default api;
