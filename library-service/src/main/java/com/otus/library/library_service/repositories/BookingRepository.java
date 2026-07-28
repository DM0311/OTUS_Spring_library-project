package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByBookId(Long bookId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
    Optional<Booking> findActiveBookingByUserAndBook(@Param("userId") Long userId,
                                                     @Param("bookId") Long bookId,
                                                     @Param("status") BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.book.id = :bookId AND b.status IN ('ACTIVE', 'OVERDUE')")
    long countActiveBookingsByBookId(@Param("bookId") Long bookId);

    List<Booking> findByStatusAndDueDateBefore(BookingStatus status, LocalDateTime date);
}
