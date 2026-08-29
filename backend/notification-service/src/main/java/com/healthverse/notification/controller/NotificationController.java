package com.healthverse.notification.controller;

import com.healthverse.notification.dto.NotificationDto;
import com.healthverse.notification.dto.NotificationEvent;
import com.healthverse.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null || authentication.getCredentials() == null) {
            throw new RuntimeException("Unauthorized: No user ID found in context");
        }
        return Long.parseLong(authentication.getCredentials().toString());
    }

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationEvent event, Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        log.info("REST request to create notification for userId: {}", userId);
        NotificationDto result = notificationService.createNotification(event, userId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUserNotifications(Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        log.info("REST request to get notifications for userId: {}", userId);
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        log.info("REST request to get notification {} for userId: {}", id, userId);
        return ResponseEntity.ok(notificationService.getNotificationById(id, userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        log.info("REST request to mark notification {} as read for userId: {}", id, userId);
        return ResponseEntity.ok(notificationService.markAsRead(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserIdFromAuth(authentication);
        log.info("REST request to delete notification {} for userId: {}", id, userId);
        notificationService.deleteNotification(id, userId);
        return ResponseEntity.noContent().build();
    }
}
