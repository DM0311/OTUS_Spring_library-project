package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthorReqDto(

        @NotBlank(message = "Полное имя автора обязательно")
        String fullName
) {
}
