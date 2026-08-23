package com.healthverse.notification.kafka;

import com.healthverse.notification.dto.NotificationEvent;
import com.healthverse.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "healthverse.notifications", groupId = "notification-group")
    public void consumeNotification(NotificationEvent event) {
        log.info("Consumed notification event for userId: {}", event.getUserId());
        notificationService.processAndSaveEvent(event);
    }

    @KafkaListener(topics = "healthverse.reminders", groupId = "notification-group")
    public void consumeReminder(NotificationEvent event) {
        log.info("Consumed reminder event for userId: {}", event.getUserId());
        notificationService.processAndSaveEvent(event);
    }

    @KafkaListener(topics = "healthverse.alerts", groupId = "notification-group")
    public void consumeAlert(NotificationEvent event) {
        log.info("Consumed alert event for userId: {}", event.getUserId());
        notificationService.processAndSaveEvent(event);
    }
}
