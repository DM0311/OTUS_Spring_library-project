package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.UserReqDto;
import com.otus.library.library_service.dto.response.UserRespDto;
import com.otus.library.library_service.events.AuditEvent;
import com.otus.library.library_service.model.entity.User;
import com.otus.library.library_service.model.enums.ActionType;
import com.otus.library.library_service.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "User controller")
public class UserController {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    @Operation(summary = "getAllUsers", description = "Get all registered users")
    @GetMapping("api/user")
    public List<UserRespDto> getAllUsers() {
        return userService.findAll();
    }

    @Operation(summary = "getUserById", description = "Get user by ID")
    @GetMapping("api/user/{id}")
    public UserRespDto getUserById(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @Operation(summary = "createUser", description = "Register/add new user")
    @PostMapping("api/user/register")
    public UserRespDto createUser(@Valid @RequestBody UserReqDto request, HttpServletRequest servletRequest) {
        UserRespDto userRespDto = userService.create(request);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.REGISTER)
                .entityType(User.class.getSimpleName())
                .entityId(userRespDto.id())
                .details("Создан пользователь " + userRespDto.fullName() + " с id: " + userRespDto.id())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return userRespDto;
    }

    @Operation(summary = "deleteUser", description = "Delete user from library")
    @DeleteMapping("api/user/{id}")
    public void deleteUser(@PathVariable("id") long id, HttpServletRequest servletRequest) {
        userService.deleteById(id);
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.USER_DELETE)
                .entityType(User.class.getSimpleName())
                .entityId(id)
                .details("Удалён пользователь с id: " + id)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
    }
}
