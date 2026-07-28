package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.BookingReqDto;
import com.otus.library.library_service.dto.response.BookingRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Booking controller")
public class BookingController {

    private final BookingService bookingService;

    private final ApplicationEventPublisher eventPublisher;

    @Operation(summary = "createBooking", description = "Create booking - take a book for reading.")
    @PostMapping("/api/booking")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingRespDto createBooking(@Valid @RequestBody BookingReqDto request,
                                        Authentication authentication,
                                        HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.createBooking(
                request.userId(),
                request.bookId(),
                request.daysToAdd()
        );
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.BOOKING_CREATE)
                .entityType(Booking.class.getSimpleName())
                .entityId(bookingResp.id())
                .details(String.format("Пользователь %d взял книгу %d", request.userId(), request.bookId()))
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );

        return bookingResp;
    }

    @Operation(summary = "prolongBooking", description = "Prolong reading time.")
    @PutMapping("/api/booking/{id}/prolong")
    public BookingRespDto prolongBooking(@PathVariable Long id,
                                         Authentication authentication,
                                         HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.prolongBooking(id);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.BOOKING_EXTEND)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Продлено бронирование " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );
        return bookingResp;
    }

    @Operation(summary = "returnBook", description = "Return book back.")
    @PutMapping("/api/booking/{id}/return")
    public BookingRespDto returnBook(@PathVariable Long id,
                                     Authentication authentication,
                                     HttpServletRequest httpRequest) {
        BookingRespDto bookingResp = bookingService.returnBook(id);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.BOOKING_RETURN)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Возврат книги по бронированию " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build());
        return bookingResp;
    }

    @Operation(summary = "cancelBooking", description = "Cancel booking - return book back")
    @DeleteMapping("/api/booking/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id,
                              Authentication authentication,
                              HttpServletRequest httpRequest) {
        bookingService.cancelBooking(id);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.BOOKING_CANCEL)
                .entityType(Booking.class.getSimpleName())
                .entityId(id)
                .details("Отмена бронирования " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build());
    }

    @Operation(summary = "getMyBookings", description = "Get all bookings of current user.")
    @GetMapping("/api/booking/my")
    public List<BookingRespDto> getMyBookings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return bookingService.getBookingsByUser(user.getId());
    }

    @Operation(summary = "getAllBookings", description = "Get all bookings.")
    @GetMapping("/api/booking/admin")
    public List<BookingRespDto> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @Operation(summary = "getBookingById", description = "Get definite booking by ID.")
    @GetMapping("/api/booking/admin/{bookingId}")
    public BookingRespDto getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }
}
