package com.otus.library.library_auth_service.dto;

public record AuthRequest(
        String username,
        String password
) {
}
