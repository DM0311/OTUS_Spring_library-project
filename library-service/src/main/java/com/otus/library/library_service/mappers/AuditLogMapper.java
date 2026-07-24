package com.otus.library.library_service.mappers;

import com.otus.library.library_service.dto.response.AuditLogRespDto;
import com.otus.library.library_service.model.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.fullName", target = "userFullName")
    AuditLogRespDto toResponse(AuditLog auditLog);
}
