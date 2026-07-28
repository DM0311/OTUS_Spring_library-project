package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами:")
@DataJpaTest
public class GenreRepositoryTest {

    private static final int GENRES_INITIAL_COUNT = 8;

    @Autowired
    private GenreRepository repository;

    @Test
    @DisplayName("- должен найти все жанры")
    void shouldFindAllGenres() {
        List<Genre> genres = repository.findAll();
        assertThat(genres).hasSize(GENRES_INITIAL_COUNT);
    }

    @Test
    @DisplayName("- должен сохранить новый жанр")
    void shouldCreateGenre(){
        Genre expected = new Genre(9L, "Test_Genre");
        Genre newGenre = new Genre();
        newGenre.setName("Test_Genre");
        Genre saved = repository.save(newGenre);
        assertThat(expected).isEqualTo(saved);
    }

    @Test
    @DisplayName("- должен удалить жанр")
    void shouldDeleGenre(){
        repository.deleteById(1L);
        List<Genre> genreList = repository.findAll();
        assertThat(genreList.size()).isEqualTo(GENRES_INITIAL_COUNT-1);
    }
}
