package com.otus.library.library_service.dto.response;

import java.time.LocalDateTime;
import java.util.Set;


public record BookRespDto(
        Long id,

        String title,

        String year,

        String description,

        Integer totalCopies,

        Integer availableCopies,

        Double rating,

        LocalDateTime createdAt,

        Set<AuthorRespDto> authors,

        Set<GenreRespDto> genres
) {

}
