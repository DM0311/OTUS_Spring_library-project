package com.otus.library.library_service.services.impl;

import com.otus.library.library_service.dto.request.CommentReqDto;
import com.otus.library.library_service.dto.response.CommentRespDto;
import com.otus.library.library_service.exception.ResourceNotFoundException;
import com.otus.library.library_service.mappers.CommentMapper;
import com.otus.library.library_service.model.entity.Book;
import com.otus.library.library_service.model.entity.Comment;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.repositories.BookRepository;
import com.otus.library.library_service.repositories.CommentRepository;
import com.otus.library.library_service.repositories.UserRepository;
import com.otus.library.library_service.services.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    private final CommentMapper mapper;

    @Override
    @Transactional
    public CommentRespDto createComment(CommentReqDto request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBook(book);
        comment.setRating(request.rating());
        comment.setCommentText(request.commentText());

        CommentRespDto saved = mapper.toResponse(commentRepository.save(comment));
        recalculateBookRating(request.bookId());

        return saved;
    }

    @Override
    @Transactional
    public CommentRespDto updateComment(long id, CommentReqDto request) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден"));
        comment.setRating(request.rating());
        comment.setCommentText(request.commentText());
        Comment updated = commentRepository.save(comment);
        recalculateBookRating(comment.getBook().getId());
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteComment(long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Комментарий не найден"));
        Long bookId = comment.getBook().getId();
        commentRepository.delete(comment);
        recalculateBookRating(bookId);
    }

    @Override
    @Transactional
    public List<CommentRespDto> getCommentsByBook(long id) {
        return commentRepository.findByBookId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public List<CommentRespDto> getCommentsByUser(long id) {
        return commentRepository.findByUserId(id)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private void recalculateBookRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Книга не найдена"));
        List<Comment> comments = commentRepository.findByBookId(bookId);
        double avg = comments.stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);
        book.setRating(avg);
        bookRepository.save(book);
    }
}
