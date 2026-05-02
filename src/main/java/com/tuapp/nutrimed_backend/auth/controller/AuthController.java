package com.tuapp.nutrimed_backend.auth.controller;

import com.tuapp.nutrimed_backend.auth.dto.*;
import com.tuapp.nutrimed_backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> google(
            @RequestBody @Valid GoogleAuthRequest req) throws Exception {
        return ResponseEntity.ok(authService.loginWithGoogle(req.idToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(authService.loginWithEmail(req));
    }
}