package com.tuapp.nutrimed_backend.user.controller;

import com.tuapp.nutrimed_backend.disease.dto.DiseaseDto;
import com.tuapp.nutrimed_backend.disease.repository.DiseaseRepository;
import com.tuapp.nutrimed_backend.user.dto.OnboardingRequest;
import com.tuapp.nutrimed_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService       userService;
    private final DiseaseRepository diseaseRepository;

    // Obtener todas las enfermedades disponibles
    @GetMapping("/diseases")
    public ResponseEntity<List<DiseaseDto>> getDiseases() {
        List<DiseaseDto> list = diseaseRepository
                .findByIsActiveTrue()
                .stream()
                .map(d -> new DiseaseDto(
                        d.getId(), d.getName(), d.getSlug(),
                        d.getDescription(), d.getCategory(), d.getIconCode()
                ))
                .toList();
        return ResponseEntity.ok(list);
    }

    // Completar onboarding
    @PostMapping("/onboarding/{userId}")
    public ResponseEntity<Map<String, Object>> completeOnboarding(
            @PathVariable Long userId,
            @RequestBody OnboardingRequest req) {
        userService.completeOnboarding(userId, req);
        return ResponseEntity.ok(Map.of(
                "message", "Onboarding completado",
                "onboardingDone", true
        ));
    }

    // Verificar si necesita onboarding
    @GetMapping("/onboarding/{userId}/needed")
    public ResponseEntity<Map<String, Boolean>> checkOnboarding(
            @PathVariable Long userId) {
        return ResponseEntity.ok(Map.of(
                "needed", userService.needsOnboarding(userId)
        ));
    }
}