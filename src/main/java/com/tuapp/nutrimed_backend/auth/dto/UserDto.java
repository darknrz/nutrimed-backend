package com.tuapp.nutrimed_backend.auth.dto;

public record UserDto(
        Long   id,
        String email,
        String username,
        String name,
        String picture,
        boolean onboardingDone
) {}