package com.otus.library.library_service.services;

import com.otus.library.library_service.model.entity.Notification;

import java.util.List;

public interface NotificationService {

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    List<Notification> getAllNotificationsByUser(Long userId);
}
