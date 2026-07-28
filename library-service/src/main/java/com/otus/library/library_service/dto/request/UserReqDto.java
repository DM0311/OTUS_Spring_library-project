package com.otus.library.library_service.dto.request;

import com.otus.library.library_service.model.enums.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UserReqDto(

        @NotBlank(message = "e-mail обязателен")
        String email,

        @NotBlank(message = "имя пользователя обязателен")
        String userName,

        @NotBlank(message = "ФИО пользователя обязателен")
        String fullName,

        Set<Role> roles,

        String password
) {
}
