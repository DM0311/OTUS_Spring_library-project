package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookingReqDto(

        @NotNull(message = "ID пользователя обязательно")
        long userId,

        @NotNull(message = "ID книги обязательно")
        long bookId,

        int daysToAdd
) {
}
