package com.otus.library.library_service.listeners;

import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.AuditLog;
import com.otus.library.library_service.repositories.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AuditEventListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @Transactional
    @EventListener
    public void handleAuditEvent(AuditEvent event) {
        AuditLog log = new AuditLog();
        log.setUser(event.getUser());
        log.setActionType(event.getActionType());
        log.setEntityType(event.getEntityType());
        log.setEntityId(event.getEntityId());
        log.setDetails(event.getDetails());
        log.setEndpoint(event.getEndpoint());
        log.setHttpMethod(event.getHttpMethod());

        auditLogRepository.save(log);
    }
}