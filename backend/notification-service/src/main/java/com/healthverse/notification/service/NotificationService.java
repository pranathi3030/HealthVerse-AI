package com.healthverse.notification.service;

import com.healthverse.notification.dto.NotificationDto;
import com.healthverse.notification.dto.NotificationEvent;

import java.util.List;

public interface NotificationService {
    void processAndSaveEvent(NotificationEvent event);
    NotificationDto createNotification(NotificationEvent event, Long userId);
    List<NotificationDto> getUserNotifications(Long userId);
    NotificationDto getNotificationById(Long id, Long userId);
    NotificationDto markAsRead(Long id, Long userId);
    void deleteNotification(Long id, Long userId);
}
