package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами:")
@DataJpaTest
public class AuthorRepositoryTest {

    private static final int AUTHORS_INITIAL_COUNT = 8;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("- должен найти всех авторов")
    void shouldFindAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(AUTHORS_INITIAL_COUNT);
    }

    @Test
    @DisplayName("- должен создать нового автора")
    void shouldCreateAuthor() {
        Author author = new Author();
        author.setFullName("Алексей Толстой");
        Author saved = authorRepository.save(author);
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(AUTHORS_INITIAL_COUNT+1);
        assertThat(saved.getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("- должен удалить автора")
    void shouldDeleteAuthor() {
        authorRepository.deleteById(9L);
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(AUTHORS_INITIAL_COUNT);
    }
}
