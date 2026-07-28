package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.ActionType;

import java.time.LocalDateTime;

public record AuditLogRespDto(

        Long id,

        Long userId,

        String userName,

        String userFullName,

        ActionType actionType,

        String entityType,

        Long entityId,

        String details,

        String endpoint,

        String httpMethod,

        LocalDateTime createdAt
) {
}
