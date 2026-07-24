package com.otus.library.library_service.repositories;

import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Comment;
import com.otus.library.library_service.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями:")
@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void setupAllData(@Autowired CommentRepository commentRepository,
                             @Autowired BookRepository bookRepository,
                             @Autowired UserRepository userRepository){
        User user1 = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User user2 = userRepository.findById(2L)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Comment comment1 = new Comment();
        comment1.setUser(user1);
        comment1.setBook(book);
        comment1.setRating(3);
        comment1.setCommentText("Comment 1");

        Comment comment2 = new Comment();
        comment2.setUser(user2);
        comment2.setBook(book);
        comment2.setRating(4);
        comment2.setCommentText("Comment 2");

        commentRepository.saveAll(List.of(comment1, comment2));

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(2);

    }

    @Test
    @DisplayName("- должен создать комментарий")
    void should() {
        User user1 = userRepository.findById(3L)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Book book = bookRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Comment comment3 = new Comment();
        comment3.setUser(user1);
        comment3.setBook(book);
        comment3.setRating(3);
        comment3.setCommentText("Comment 3");

        commentRepository.save(comment3);

        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(3);
    }

    @Test
    @DisplayName("- должен найти комментарии по книге")
    void shouldFindCommentsByBook() {
        List<String> comments = commentRepository.findByBookId(1L)
                .stream()
                .map(Comment::getCommentText)
                .toList();

        assertThat(comments).hasSize(2);
        assertThat(comments).containsExactly("Comment 1", "Comment 2");
    }

    @Test
    @DisplayName("- должен найти комментарии пользователя")
    void shouldFindCommentsByUser() {
        List<String> comments = commentRepository.findByUserId(1L)
                .stream()
                .map(Comment::getCommentText)
                .toList();

        assertThat(comments).hasSize(1);
        assertThat(comments).containsExactly("Comment 1");
    }

    @Test
    @DisplayName("- должен удалить комментарий")
    void shouldDeleteComment() {
        commentRepository.deleteById(1L);
        List<String> comments = commentRepository.findAll()
                .stream()
                .map(Comment::getCommentText)
                .toList();

        assertThat(comments).hasSize(1);
        assertThat(comments).containsExactly("Comment 2");
    }
}
