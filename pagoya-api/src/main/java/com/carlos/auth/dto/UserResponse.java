package com.carlos.auth.dto;

public record UserResponse(
        Long id,
        String email,
        Boolean verified,
        String role
) {}