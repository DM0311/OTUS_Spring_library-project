package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.CommentReqDto;
import com.otus.library.library_service.dto.response.CommentRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Comment;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Comment controller")
public class CommentController {

    private final CommentService commentService;

    private final ApplicationEventPublisher eventPublisher;

    @Operation(summary = "createComment", description = "Add comment to a book")
    @PostMapping("/api/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentRespDto createComment(@Valid @RequestBody CommentReqDto request,
                                        Authentication authentication,
                                        HttpServletRequest httpRequest) {

        CommentRespDto respDto = commentService.createComment(request);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.REVIEW_CREATE)
                .entityType(Comment.class.getSimpleName())
                .entityId(respDto.id())
                .details("Пользователь " + request.userId() + " оставил комментарий к книге " + request.bookId())
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );

        return respDto;
    }

    @Operation(summary = "updateComment", description = "Update/edit comment")
    @PutMapping("/api/comment/{id}")
    public CommentRespDto updateComment(@PathVariable Long id,
                                        @Valid @RequestBody CommentReqDto request,
                                        Authentication authentication,
                                        HttpServletRequest httpRequest) {
        CommentRespDto respDto = commentService.updateComment(id, request);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.REVIEW_UPDATE)
                .entityType(Comment.class.getSimpleName())
                .entityId(id)
                .details("Обновлён комментарий " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );

        return respDto;
    }

    @Operation(summary = "deleteComment", description = "Delete comment")
    @DeleteMapping("/api/comment/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id,
                              Authentication authentication,
                              HttpServletRequest httpRequest) {

        commentService.deleteComment(id);
        User user = (User) authentication.getPrincipal();
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(user)
                .actionType(ActionType.REVIEW_DELETE)
                .entityType(Comment.class.getSimpleName())
                .entityId(id)
                .details("Удалён комментарий " + id)
                .endpoint(httpRequest.getRequestURI())
                .httpMethod(httpRequest.getMethod())
                .build()
        );
    }

    @Operation(summary = "getCommentsByBook", description = "Get all comments to a book")
    @GetMapping("/api/comment/book/{bookId}")
    public List<CommentRespDto> getCommentsByBook(@PathVariable Long bookId) {
        return commentService.getCommentsByBook(bookId);
    }

    @Operation(summary = "getCommentsByUser", description = "Get all user's comments")
    @GetMapping("/api/comment/user/{userId}")
    public List<CommentRespDto> getCommentsByUser(@PathVariable Long userId) {
        return commentService.getCommentsByUser(userId);
    }
}
