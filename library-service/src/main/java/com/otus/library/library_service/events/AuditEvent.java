package com.otus.library.library_service.events;

import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuditEvent {

    private final User user;

    private final ActionType actionType;

    private final String entityType;

    private final Long entityId;

    private final String details;

    private final String endpoint;

    private final String httpMethod;

}
