package com.otus.library.library_service.services;

import com.otus.library.library_service.dto.response.BookingRespDto;

import java.util.List;

public interface BookingService {

    BookingRespDto createBooking(Long userId, Long bookId, Integer daysToAdd);

    BookingRespDto prolongBooking(Long bookingId);

    BookingRespDto returnBook(Long bookingId);

    void cancelBooking(Long bookingId);

    List<BookingRespDto> getBookingsByUser(Long userId);

    List<BookingRespDto> getAllBookings();

    public BookingRespDto getBookingById(Long id);
}
