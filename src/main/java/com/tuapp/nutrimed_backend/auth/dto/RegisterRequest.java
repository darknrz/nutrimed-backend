package com.tuapp.nutrimed_backend.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 3, max = 20)
        String username,        // nick único, ej: "mario92"

        @NotBlank @Size(min = 6)
        String password,

        String name             // nombre real, opcional
) {}