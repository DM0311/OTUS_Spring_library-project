package com.otus.library.library_auth_service.controller;

import com.otus.library.library_auth_service.dto.AuthRequest;
import com.otus.library.library_auth_service.dto.AuthResponse;
import com.otus.library.library_auth_service.dto.RefreshTokenRequest;
import com.otus.library.library_auth_service.security.CustomUserDetailsService;
import com.otus.library.library_auth_service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.username());
        String access = jwtTokenProvider.generateAccessToken(user);
        String refresh = jwtTokenProvider.generateRefreshToken(user);
        return ResponseEntity.ok(new AuthResponse(access, refresh));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String username = jwtTokenProvider.extractUsername(request.refreshToken());
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String newAccess = jwtTokenProvider.generateAccessToken(user);
        return ResponseEntity.ok(new AuthResponse(newAccess, request.refreshToken()));
    }
}
