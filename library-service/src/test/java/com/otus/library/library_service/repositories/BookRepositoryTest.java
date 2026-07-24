package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Author;
import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Genre;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами:")
@DataJpaTest
public class BookRepositoryTest {

    private static final int BOOKS_INITIAL_COUNT = 8;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("- должен найти все книги")
    void shouldFindAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(BOOKS_INITIAL_COUNT);
    }

    @Test
    @DisplayName("- должен найти книгу по ID")
    void shouldFindById() {
        Book found = bookRepository.findById(1L).
                orElseThrow(() -> new EntityNotFoundException("Book not found"));
        assertThat(found.getTitle()).isEqualTo("Война и мир");
        assertThat(found.getRating()).isEqualTo(4.8);
    }

    @Test
    @DisplayName("- должен найти книгу по условиям поиска")
    void shouldSearch() {
        List<Book> searched = bookRepository.searchBooks("война", null, null, "1869");
        assertThat(searched.get(0).getId()).isEqualTo(1L);
        assertThat(searched.get(0).getTitle()).isEqualTo("Война и мир");
    }

    @Test
    @DisplayName("- должен создать новую книгу")
    void shouldCreate() {
        Author author = authorRepository.findById(1L)
                .orElseThrow(()->new EntityNotFoundException(""));
        Genre genre = genreRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException(""));
        Book newBook = new Book();
        newBook.setTitle("Test_book");
        newBook.setYear("2026");
        newBook.setDescription("No description");
        newBook.setAuthors(Set.of(author));
        newBook.setGenres(Set.of(genre));
        newBook.setTotalCopies(2);
        newBook.setAvailableCopies(2);
        newBook.setRating(0);

        Book saved = bookRepository.save(newBook);
        assertThat(saved.getId()).isEqualTo(9L);
    }


    @Test
    @DisplayName("- должен удалить книгу")
    void shouldDelete() {
        bookRepository.deleteById(9L);
        List<Book> books = bookRepository.findAll();
        assertThat(books.size()).isEqualTo(BOOKS_INITIAL_COUNT);
    }
}
