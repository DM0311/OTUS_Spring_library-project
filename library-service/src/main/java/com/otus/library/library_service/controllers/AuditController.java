package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.response.AuditLogRespDto;
import com.otus.library.library_service.services.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Audit Log controller")
public class AuditController {

    private final AuditService auditService;

    @Operation(summary = "getAllLogs", description = "Find all available logs")
    @GetMapping("/api/admin/auditLog")
    public List<AuditLogRespDto> getAllLogs() {
        return auditService.findAllLogsDesc();
    }
}
