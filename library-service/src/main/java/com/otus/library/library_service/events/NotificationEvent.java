package com.otus.library.library_service.events;

import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationEvent {

    private final User user;

    private final String message;

    private final NotificationType type;
}
