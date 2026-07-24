package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.GenreReqDto;
import com.otus.library.library_service.dto.response.GenreRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.Genre;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.GenreService;
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
@Tag(name = "Genre controller")
public class GenreController {

    private final GenreService genreService;

    private final ApplicationEventPublisher eventPublisher;

    @Operation(summary = "getAllGenres", description = "Get all available genres")
    @GetMapping("/api/genre")
    public List<GenreRespDto> getAllGenres() {
        return genreService.findAll();
    }

    @Operation(summary = "createGenr", description = "Add a new genre")
    @PostMapping("/api/genre")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreRespDto createGenre(@Valid @RequestBody GenreReqDto request, HttpServletRequest servletRequest) {
        GenreRespDto genreRespDto = genreService.create(request);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.GENRE_CREATE)
                .entityType(Genre.class.getSimpleName())
                .entityId(genreRespDto.id())
                .details("Создан жанр " + genreRespDto.name() + " с id: " + genreRespDto.id())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return genreRespDto;
    }

    @Operation(summary = "deleteGenre", description = "Delete genre")
    @DeleteMapping("/api/genre/{id}")
    public void deleteGenre(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        genreService.deleteById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.GENRE_DELETE)
                .entityType(Genre.class.getSimpleName())
                .entityId(id)
                .details("Удален жанр с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
