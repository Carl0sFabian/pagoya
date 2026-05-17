package com.carlos.auth.dto;
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String email,
        String role,
        long accessExpiresInMs
) {}
