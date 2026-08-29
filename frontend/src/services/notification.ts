import api from './api';

export interface NotificationDto {
  id: number;
  eventId: string;
  userId: number;
  type: string;
  title: string;
  message: string;
  priority: string;
  timestamp: string;
  metadata?: string;
  read: boolean;
}

export const notificationService = {
  getUserNotifications: async (): Promise<NotificationDto[]> => {
    const response = await api.get('/api/notifications');
    return response.data;
  },
  
  markAsRead: async (id: number): Promise<NotificationDto> => {
    const response = await api.patch(`/api/notifications/${id}/read`);
    return response.data;
  }
};
