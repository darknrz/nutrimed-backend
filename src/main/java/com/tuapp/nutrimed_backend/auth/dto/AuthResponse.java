package com.tuapp.nutrimed_backend.auth.dto;

public record AuthResponse(
        String  accessToken,
        String  refreshToken,
        UserDto user
) {}