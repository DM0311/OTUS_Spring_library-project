package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationRespDto(

        Long id,

        Long userId,

        String userName,

        NotificationType type,

        String message,

        boolean isRead,

        LocalDateTime createdAt
) {
}
