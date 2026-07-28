package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateRequest(

        @NotBlank(message = "e-mail обязателен")
        String email,

        @NotBlank(message = "ФИО обязательно")
        String fullName) {
}
