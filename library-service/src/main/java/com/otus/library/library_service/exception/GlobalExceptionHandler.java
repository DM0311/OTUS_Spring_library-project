package com.otus.library.library_service.exception;

import com.otus.library.library_service.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExists(UserAlreadyExistsException exception) {

        return prepareResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException exception) {
        return prepareResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponseDto> handleBookingException(BookingException exception) {
        return prepareResponse(exception, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorResponseDto> prepareResponse(RuntimeException exception, HttpStatus status) {
        ErrorResponseDto error = new ErrorResponseDto(
                status.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status.value()).body(error);
    }
}
