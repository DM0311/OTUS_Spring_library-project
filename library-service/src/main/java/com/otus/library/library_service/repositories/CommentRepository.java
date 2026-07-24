package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBookId(Long bookId);

    List<Comment> findByUserId(Long userId);
}
