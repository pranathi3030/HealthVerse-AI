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
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    private void processMessageSafely(String messagePayload) {
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(messagePayload);
            NotificationEvent event = new NotificationEvent();
            
            event.setEventId(node.has("eventId") ? node.get("eventId").asText() : java.util.UUID.randomUUID().toString());
            event.setUserId(node.hasNonNull("userId") ? node.get("userId").asLong() : 0L);
            event.setTitle(node.has("title") ? node.get("title").asText() : "System Notification");
            event.setMessage(node.has("message") ? node.get("message").asText() : "");
            event.setPriority(node.has("priority") ? node.get("priority").asText() : "LOW");
            event.setMetadata(node.has("metadata") ? node.get("metadata").asText() : null);

            if (node.hasNonNull("type")) {
                try {
                    event.setType(com.healthverse.notification.dto.NotificationType.valueOf(node.get("type").asText()));
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown notification type received: {}. Defaulting to SYSTEM.", node.get("type").asText());
                    event.setType(com.healthverse.notification.dto.NotificationType.SYSTEM);
                }
            } else {
                event.setType(com.healthverse.notification.dto.NotificationType.SYSTEM);
            }

            log.info("Successfully parsed and dispatching event for userId: {}", event.getUserId());
            notificationService.processAndSaveEvent(event);
        } catch (Exception e) {
            log.error("Failed to parse or process notification message: {}", messagePayload, e);
        }
    }

    @KafkaListener(topics = "healthverse.notifications", groupId = "notification-group")
    public void consumeNotification(String messagePayload) {
        log.info("Consumed raw notification event: {}", messagePayload);
        processMessageSafely(messagePayload);
    }

    @KafkaListener(topics = "healthverse.reminders", groupId = "notification-group")
    public void consumeReminder(String messagePayload) {
        log.info("Consumed raw reminder event: {}", messagePayload);
        processMessageSafely(messagePayload);
    }

    @KafkaListener(topics = "healthverse.alerts", groupId = "notification-group")
    public void consumeAlert(String messagePayload) {
        log.info("Consumed raw alert event: {}", messagePayload);
        processMessageSafely(messagePayload);
    }
}
