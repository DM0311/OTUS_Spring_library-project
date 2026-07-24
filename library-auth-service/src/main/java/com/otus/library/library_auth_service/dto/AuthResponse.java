package com.otus.library.library_auth_service.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {

    public AuthResponse(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, "Bearer");
    }

}
