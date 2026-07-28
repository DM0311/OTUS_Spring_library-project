package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.response.AuditLogRespDto;
import com.otus.library.library_service.mappers.AuditLogMapper;
import com.otus.library.library_service.repositories.AuditLogRepository;
import com.otus.library.library_service.services.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper mapper;

    @Override
    @Transactional
    public List<AuditLogRespDto> findAllLogsDesc() {
        return auditLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
