package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GenreReqDto(

        @NotBlank(message = "Название жанра обязательно")
        String name
) {
}
