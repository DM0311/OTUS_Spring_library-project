package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorRepository extends JpaRepository<Author, Long> {

}
