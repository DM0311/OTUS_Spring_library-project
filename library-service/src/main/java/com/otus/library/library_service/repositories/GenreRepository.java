package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
