package com.otus.library.library_service.listeners;

import com.otus.library.library_service.events.NotificationEvent;
import com.otus.library.library_service.model.entity.Notification;
import com.otus.library.library_service.repositories.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationEventListener {

    private final NotificationRepository notificationRepository;

    @Async
    @Transactional
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        Notification notification = new Notification();
        notification.setUser(event.getUser());
        notification.setMessage(event.getMessage());
        notification.setType(event.getType());
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
