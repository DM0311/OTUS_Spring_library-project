package com.otus.library.library_service.dto.response;

import com.otus.library.library_service.model.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingRespDto(

        Long id,

        Long userId,

        String userName,

        Long bookId,

        String bookTitle,

        LocalDateTime bookingDate,

        LocalDateTime dueDate,

        LocalDateTime actualReturnDate,

        BookingStatus status,

        boolean extended,

        int extensionCount,

        LocalDateTime createdAt

) {
}
