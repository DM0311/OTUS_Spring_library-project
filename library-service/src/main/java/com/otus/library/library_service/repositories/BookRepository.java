package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book-with-author-genres-graph")
    Optional<Book> findById(long id);

    @EntityGraph("book-with-author-graph")
    List<Book> findAll();

    @Query(value = "SELECT DISTINCT b.* FROM books b " +
            "LEFT JOIN books_authors ba ON b.id = ba.book_id " +
            "LEFT JOIN authors a ON ba.author_id = a.id " +
            "LEFT JOIN books_genres bg ON b.id = bg.book_id " +
            "LEFT JOIN genres g ON bg.genre_id = g.id " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', CAST(:title AS text), '%'))) " +
            "AND (:author IS NULL OR LOWER(a.full_name) LIKE LOWER(CONCAT('%', CAST(:author AS text), '%'))) " +
            "AND (:genreId IS NULL OR g.id = CAST(:genreId AS bigint)) " +
            "AND (:publicationYear IS NULL OR b.publication_year = CAST(:publicationYear AS text))", // ИСПРАВЛЕНО
            nativeQuery = true)
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("genreId") Long genreId,
                           @Param("publicationYear") String publicationYear);
}
