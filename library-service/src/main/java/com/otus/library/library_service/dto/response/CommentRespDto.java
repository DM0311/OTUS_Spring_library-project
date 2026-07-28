package com.otus.library.library_service.dto.response;

import java.time.LocalDateTime;


public record CommentRespDto(

        Long id,

        Long userId,

        String userName,

        Long bookId,

        String bookTitle,

        int rating,

        String commentText,

        LocalDateTime createdAt
) {

}
