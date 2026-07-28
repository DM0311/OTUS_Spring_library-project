package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.response.BookingRespDto;
import com.otus.library.library_service.exception.BookingException;
import com.otus.library.library_service.exception.ResourceNotFoundException;
import com.otus.library.library_service.mappers.BookingMapper;
import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Booking;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.BookingStatus;
import com.otus.library.library_service.repositories.BookRepository;
import com.otus.library.library_service.repositories.BookingRepository;
import com.otus.library.library_service.repositories.UserRepository;
import com.otus.library.library_service.services.BookingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    private final BookingMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public BookingRespDto createBooking(Long userId, Long bookId, Integer daysToAdd) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена"));

        check(user, book);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBook(book);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setBookingDate(LocalDateTime.now());
        daysToAdd = (daysToAdd == null || daysToAdd <= 0) ? 14 : daysToAdd;
        booking.setDueDate(LocalDateTime.now().plusDays(daysToAdd));

        book.decreaseAvailableCopies();
        bookRepository.save(book);

        Booking saved = bookingRepository.save(booking);
        entityManager.refresh(saved);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BookingRespDto prolongBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование не найдено"));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new BookingException("Продлить можно только активное бронирование");
        }
        if (booking.getExtensionCount() >= 1) {
            throw new BookingException("Продление уже было использовано");
        }

        booking.setDueDate(booking.getDueDate().plusDays(14));
        booking.setExtensionCount(booking.getExtensionCount() + 1);
        booking.setExtended(true);
        Booking saved = bookingRepository.save(booking);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BookingRespDto returnBook(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование не найдено"));

        if (booking.getStatus() != BookingStatus.ACTIVE && booking.getStatus() != BookingStatus.OVERDUE) {
            throw new BookingException("Нельзя вернуть книгу в статусе: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.RETURNED);
        booking.setActualReturnDate(LocalDateTime.now());

        Book book = booking.getBook();
        book.increaseAvailableCopies();
        bookRepository.save(book);

        Booking saved = bookingRepository.save(booking);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование не найдено"));

        if (booking.getStatus() == BookingStatus.RETURNED) {
            throw new BookingException("Нельзя отменить уже возвращённую книгу");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        if (booking.getStatus() == BookingStatus.ACTIVE) {
            Book book = booking.getBook();
            book.increaseAvailableCopies();
            bookRepository.save(book);
        }

        bookingRepository.save(booking);
    }

    public List<BookingRespDto> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<BookingRespDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BookingRespDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Бронирование не найдено"));
    }

    private void check(User user, Book book) {

        if (user.isBlocked()) {
            throw new BookingException("Пользователь заблокирован");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new BookingException("Нет доступных экземпляров книги");
        }

        if (bookingRepository.findActiveBookingByUserAndBook(user.getId(),
                book.getId(),
                BookingStatus.ACTIVE).isPresent()) {
            throw new BookingException("У вас уже есть активное бронирование этой книги");
        }
    }
}
