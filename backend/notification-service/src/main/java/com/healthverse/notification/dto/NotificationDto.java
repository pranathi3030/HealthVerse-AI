package com.healthverse.notification.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private String eventId;
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private String priority;
    private LocalDateTime timestamp;
    private String metadata;
    private boolean isRead;
}
