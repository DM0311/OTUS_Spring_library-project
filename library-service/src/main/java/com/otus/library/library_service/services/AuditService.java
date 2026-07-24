package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.response.AuditLogRespDto;

import java.util.List;

public interface AuditService {

    List<AuditLogRespDto> findAllLogsDesc();
}
