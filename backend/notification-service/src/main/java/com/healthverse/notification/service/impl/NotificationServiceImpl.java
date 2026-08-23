package com.healthverse.notification.service.impl;

import com.healthverse.notification.dto.NotificationDto;
import com.healthverse.notification.dto.NotificationEvent;
import com.healthverse.notification.entity.Notification;
import com.healthverse.notification.exception.ResourceNotFoundException;
import com.healthverse.notification.kafka.NotificationProducer;
import com.healthverse.notification.repository.NotificationRepository;
import com.healthverse.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProducer notificationProducer;

    @Override
    @Transactional
    public void processAndSaveEvent(NotificationEvent event) {
        log.info("Processing event: {}", event.getEventId());
        
        Notification notification = Notification.builder()
                .eventId(event.getEventId())
                .userId(event.getUserId())
                .type(event.getType())
                .title(event.getTitle())
                .message(event.getMessage())
                .priority(event.getPriority())
                .timestamp(event.getTimestamp() != null ? event.getTimestamp() : LocalDateTime.now())
                .metadata(event.getMetadata())
                .isRead(false)
                .build();
                
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public NotificationDto createNotification(NotificationEvent event, Long userId) {
        if (event.getEventId() == null) {
            event.setEventId(UUID.randomUUID().toString());
        }
        event.setUserId(userId);
        
        if (event.getTimestamp() == null) {
            event.setTimestamp(LocalDateTime.now());
        }

        Notification notification = new Notification();
        notification.setEventId(event.getEventId());
        notification.setUserId(userId);
        notification.setType(event.getType());
        notification.setTitle(event.getTitle());
        notification.setMessage(event.getMessage());
        notification.setPriority(event.getPriority() != null ? event.getPriority() : "LOW");
        notification.setTimestamp(event.getTimestamp());
        notification.setMetadata(event.getMetadata());
        notification.setRead(false);
        notification = notificationRepository.save(notification);

        switch (event.getType()) {
            case MEDICINE_REMINDER:
            case HEALTH_REMINDER:
            case WELLNESS_REMINDER:
            case FITNESS_REMINDER:
            case NUTRITION_REMINDER:
                notificationProducer.publishReminder(event);
                break;
            case REPORT_READY:
                notificationProducer.publishAlert(event);
                break;
            default:
                notificationProducer.publishNotification(event);
        }

        return NotificationDto.builder()
                .id(notification.getId())
                .eventId(notification.getEventId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .priority(notification.getPriority())
                .timestamp(notification.getTimestamp())
                .metadata(notification.getMetadata())
                .isRead(notification.isRead())
                .build();
    }

    @Override
    public List<NotificationDto> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByTimestampDesc(userId);
        return notifications.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public NotificationDto getNotificationById(Long id, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return mapToDto(notification);
    }

    @Override
    @Transactional
    public NotificationDto markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return mapToDto(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }

    private NotificationDto mapToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .eventId(notification.getEventId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .priority(notification.getPriority())
                .timestamp(notification.getTimestamp())
                .metadata(notification.getMetadata())
                .isRead(notification.isRead())
                .build();
    }
}
