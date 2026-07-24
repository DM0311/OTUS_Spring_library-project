package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.BookReqDto;
import com.otus.library.library_service.dto.response.BookRespDto;
import com.otus.library.library_service.exception.ResourceNotFoundException;
import com.otus.library.library_service.mappers.BookMapper;
import com.otus.library.library_service.model.entity.Author;
import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Genre;
import com.otus.library.library_service.repositories.AuthorRepository;
import com.otus.library.library_service.repositories.BookRepository;
import com.otus.library.library_service.repositories.GenreRepository;
import com.otus.library.library_service.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<BookRespDto> findAll() {
        return bookRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookRespDto findById(long id) {
        return bookRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена, id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookRespDto> searchBooks(String title, String author, Long genreId, String year) {
        return bookRepository.searchBooks(title, author, genreId, year)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BookRespDto create(BookReqDto request) {

        Book book = new Book();
        book.setTitle(request.title());
        book.setYear(request.year());
        book.setDescription(request.description());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.availableCopies());
        book.setRating(0.0);

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(request.authorIds()));
        book.setAuthors(authors);

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.genreIds()));
        book.setGenres(genres);

        Book saved = bookRepository.save(book);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public BookRespDto updateBook(Long id, BookReqDto request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена. id: " + id));

        book.setTitle(request.title());
        book.setYear(request.year());
        book.setDescription(request.description());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.availableCopies());

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(request.authorIds()));
        book.setAuthors(authors);

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(request.genreIds()));
        book.setGenres(genres);

        Book saved = bookRepository.save(book);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleById(long id) {
        bookRepository.deleteById(id);
    }
}
