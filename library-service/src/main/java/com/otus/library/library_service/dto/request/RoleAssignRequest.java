package com.otus.library.library_service.dto.request;

import com.otus.library.library_service.model.enums.Role;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record RoleAssignRequest(

        @NotNull(message = "Список ролей обязателен")
        Set<Role> roles
) {
}
