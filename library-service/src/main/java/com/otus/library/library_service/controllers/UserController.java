package com.otus.library.library_service.controllers;

import com.otus.library.library_service.dto.request.PasswordChangeRequest;
import com.otus.library.library_service.dto.request.ProfileUpdateRequest;
import com.otus.library.library_service.dto.request.RoleAssignRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Operation(summary = "changePassword", description = "Change password")
    @PutMapping("/api/user/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request,
                                               HttpServletRequest servletRequest,
                                               Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        userService.changePassword(currentUser.getId(), request.currentPassword(), request.newPassword());
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.USER_PASSWORD_CHANGE)
                .entityType(User.class.getSimpleName())
                .entityId(currentUser.getId())
                .details("Смена пароля - пользователь " + currentUser.getFullName() + " с id: " + currentUser.getId())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "changeProfile", description = "Change name/e-mail/etc...")
    @PutMapping("/api/user/profile")
    public UserRespDto changeProfile(@Valid @RequestBody ProfileUpdateRequest request,
                                     HttpServletRequest servletRequest,
                                     Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        UserRespDto userRespDto = userService.updateProfile(currentUser.getId(), request.email(), request.fullName());
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(null)
                .actionType(ActionType.USER_UPDATE)
                .entityType(User.class.getSimpleName())
                .entityId(currentUser.getId())
                .details("Обновление профиля - пользователь " + currentUser.getFullName() + " с id: " + currentUser.getId())
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());

        return userRespDto;
    }

    @Operation(summary = "assignRoles", description = "Assign new roles to user")
    @PutMapping("/api/admin/{userId}/roles")
    public UserRespDto assignNewRoles(@PathVariable Long userId,
                                      @Valid @RequestBody RoleAssignRequest request,
                                      HttpServletRequest servletRequest,
                                      Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();
        UserRespDto userRespDto = userService.assignRoles(userId, request.roles());
        eventPublisher.publishEvent(AuditEvent.builder()
                .user(currentUser)
                .actionType(ActionType.USER_ROLE_CHANGE)
                .entityType(User.class.getSimpleName())
                .entityId(userId)
                .details("Обновлены роли пользователя с id: " + userId)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return userRespDto;
    }
    @Operation(summary = "blockUser", description = "Block user")
    @PutMapping("api/admin/{userId}/block")
    public UserRespDto blockUser(@PathVariable Long userId,
                                 HttpServletRequest servletRequest,
                                 Authentication authentication){

        User currentUser = (User) authentication.getPrincipal();

        UserRespDto blocked = userService.block(userId, true);

        eventPublisher.publishEvent(AuditEvent.builder()
                .user(currentUser)
                .actionType(ActionType.USER_BLOCK)
                .entityType(User.class.getSimpleName())
                .entityId(userId)
                .details("Заблокировать пользователь с id: " + userId)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return blocked;
    }

    @Operation(summary = "unblockUser", description = "Unblock user")
    @PutMapping("api/admin/{userId}/unblock")
    public UserRespDto unblockUser(@PathVariable Long userId,
                                   HttpServletRequest servletRequest,
                                   Authentication authentication){

        User currentUser = (User) authentication.getPrincipal();

        UserRespDto blocked = userService.block(userId, false);

        eventPublisher.publishEvent(AuditEvent.builder()
                .user(currentUser)
                .actionType(ActionType.USER_UNBLOCK)
                .entityType(User.class.getSimpleName())
                .entityId(userId)
                .details("Разблокирован пользователь с id: " + userId)
                .endpoint(servletRequest.getRequestURI())
                .httpMethod(servletRequest.getMethod())
                .build());
        return blocked;
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
