package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record CommentReqDto(

        @NotNull(message = "ID пользователя обязательно")
        long userId,

        @NotNull(message = "ID книги обязательно")
        long bookId,

        @Min(value = 1, message = "Рейтинг должен быть от 1 до 5")
        @Max(value = 5, message = "Рейтинг должен быть от 1 до 5")
        int rating,

        String commentText
) {
}
