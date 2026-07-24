package com.otus.library.library_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record BookReqDto(

        @NotBlank(message = "Название обязательно")
        String title,

        String year,

        String description,

        @NotNull(message = "Укажите общее количество экземпляров")
        @Min(value = 1, message = "Количество должно быть больше 0")
        int totalCopies,

        @NotNull(message = "Укажите количество доступных экземпляров")
        @Min(value = 0, message = "Количество не может быть отрицательным")
        int availableCopies,

        Set<Long> authorIds,

        Set<Long> genreIds
) {
}
