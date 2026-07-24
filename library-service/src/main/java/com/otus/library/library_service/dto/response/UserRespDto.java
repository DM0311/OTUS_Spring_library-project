package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

public record UserRespDto(

        long id,

        String email,

        String userName,

        String fullName,

        boolean isBlocked,

        LocalDateTime blockedUntil,

        int penaltyPoints,

        Set<Role> roles,

        LocalDateTime createdAt
) {
}
