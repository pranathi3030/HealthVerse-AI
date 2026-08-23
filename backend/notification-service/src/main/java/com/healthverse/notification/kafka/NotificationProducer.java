package com.healthverse.notification.kafka;

import com.healthverse.notification.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void publishNotification(NotificationEvent event) {
        log.info("Producing notification event for userId: {}", event.getUserId());
        try {
            kafkaTemplate.send("healthverse.notifications", event.getEventId(), event).get();
            log.info("Sent notification event successfully");
        } catch (Exception e) {
            log.error("Failed to send notification event", e);
        }
    }

    public void publishReminder(NotificationEvent event) {
        log.info("Producing reminder event for userId: {}", event.getUserId());
        kafkaTemplate.send("healthverse.reminders", event.getEventId(), event);
    }

    public void publishAlert(NotificationEvent event) {
        log.info("Producing alert event for userId: {}", event.getUserId());
        kafkaTemplate.send("healthverse.alerts", event.getEventId(), event);
    }
}
