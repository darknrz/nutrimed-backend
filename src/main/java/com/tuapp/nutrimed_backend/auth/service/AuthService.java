package com.tuapp.nutrimed_backend.auth.service;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.tuapp.nutrimed_backend.auth.dto.*;
import com.tuapp.nutrimed_backend.user.entity.User;
import com.tuapp.nutrimed_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleIdTokenVerifier verifier;
    private final UserRepository        userRepository;
    private final JwtService            jwtService;
    private final PasswordEncoder       passwordEncoder;

    // ── LOGIN CON GOOGLE ─────────────────────────────────────
    public AuthResponse loginWithGoogle(String idToken) throws Exception {
        GoogleIdToken googleToken = verifier.verify(idToken);
        if (googleToken == null) {
            throw new RuntimeException("Token de Google inválido");
        }

        GoogleIdToken.Payload payload = googleToken.getPayload();
        String email = payload.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    String baseName = email.split("@")[0];
                    String username = generateUniqueUsername(baseName);
                    return userRepository.save(
                            User.builder()
                                    .email(email)
                                    .username(username)
                                    .name((String) payload.get("name"))
                                    .picture((String) payload.get("picture"))
                                    .provider(User.AuthProvider.GOOGLE)
                                    .build()
                    );
                });

        return buildResponse(user);
    }

    // ── REGISTRO CON EMAIL ───────────────────────────────────
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (userRepository.existsByUsername(req.username())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        User user = userRepository.save(
                User.builder()
                        .email(req.email())
                        .username(req.username())
                        .name(req.name() != null ? req.name() : req.username())
                        .password(passwordEncoder.encode(req.password()))
                        .provider(User.AuthProvider.EMAIL)
                        .build()
        );

        return buildResponse(user);
    }

    // ── LOGIN CON EMAIL ──────────────────────────────────────
    public AuthResponse loginWithEmail(LoginRequest req) {
        User user = userRepository
                .findByEmailOrUsername(req.emailOrUsername(), req.emailOrUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return buildResponse(user);
    }

    // ── HELPERS ──────────────────────────────────────────────
    private AuthResponse buildResponse(User user) {
        return new AuthResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user),
                new UserDto(
                        user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getName(),
                        user.getPicture(),
                        user.isOnboardingDone()
                )
        );
    }

    private String generateUniqueUsername(String base) {
        String candidate = base;
        int i = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + i++;
        }
        return candidate;
    }
}