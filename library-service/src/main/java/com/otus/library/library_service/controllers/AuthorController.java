package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.AuthorReqDto;
import com.otus.library.library_service.dto.response.AuthorRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Author;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Author controller")
public class AuthorController {

    private final AuthorService authorService;

    private final ApplicationEventPublisher eventPublisher;

    @Operation(summary = "getAllAuthors", description = "Find all authors.")
    @GetMapping("api/author")
    List<AuthorRespDto> getAllAuthors() {
        return authorService.findAll();
    }

    @Operation(summary = "createAuthor", description = "Add new author.")
    @PostMapping("/api/author")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorRespDto createAuthor(@Valid @RequestBody AuthorReqDto request, HttpServletRequest servletRequest) {
        AuthorRespDto authorRespDto = authorService.create(request);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.AUTHOR_CREATE)
                .entityType(Author.class.getSimpleName())
                .entityId(authorRespDto.id())
                .details("Создан автор " + authorRespDto.fullName() + " с id: " + authorRespDto.id())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return authorRespDto;
    }

    @Operation(summary = "deleteAuthor", description = "Delete author by ID")
    @DeleteMapping("/api/author/{id}")
    public void deleteAuthor(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        authorService.deleteById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.AUTHOR_DELETE)
                .entityType(Author.class.getSimpleName())
                .entityId(id)
                .details("Удалён автор с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
