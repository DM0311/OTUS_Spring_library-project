package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.BookingStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с бронированием:")
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setUpData(@Autowired UserRepository userRepository,
                          @Autowired BookRepository bookRepository,
                          @Autowired BookingRepository bookingRepository){
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Book> bookList = bookRepository.findAll();

        Booking booking1 = new Booking();
        booking1.setUser(user);
        booking1.setBook(bookList.get(0));
        booking1.setBookingDate(LocalDateTime.now());
        booking1.setDueDate(LocalDateTime.now().plusDays(14));
        booking1.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setUser(user);
        booking2.setBook(bookList.get(1));
        booking2.setBookingDate(LocalDateTime.now());
        booking2.setDueDate(LocalDateTime.now().plusDays(14));
        booking2.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking2);
    }

    @Test
    @DisplayName("- должен забронировать книгу")
    void shouldCreateBooking() {



        List<Booking> bookings = bookingRepository.findAll();

        assertThat(bookings).hasSize(2);
    }

    @Test
    @DisplayName("- должен найти бронирования пользователя")
    void shouldFindUserBookings() {
        List<Booking> bookings = bookingRepository.findByUserId(1L);
        assertThat(bookings).hasSize(2);
    }

    @Test
    @DisplayName("- должен найти все бронирования")
    void shouldFindAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(2);
    }
}
