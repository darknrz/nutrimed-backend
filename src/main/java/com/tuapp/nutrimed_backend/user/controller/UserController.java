package com.tuapp.nutrimed_backend.user.controller;

import com.tuapp.nutrimed_backend.config.CloudinaryService;
import com.tuapp.nutrimed_backend.disease.dto.DiseaseDto;
import com.tuapp.nutrimed_backend.disease.repository.DiseaseRepository;
import com.tuapp.nutrimed_backend.disease.repository.UserDiseaseRepository;  // ← agrega
import com.tuapp.nutrimed_backend.user.dto.OnboardingRequest;
import com.tuapp.nutrimed_backend.user.dto.UserProfileDto;
import com.tuapp.nutrimed_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tuapp.nutrimed_backend.user.dto.UpdateProfileRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService            userService;
    private final DiseaseRepository      diseaseRepository;
    private final UserDiseaseRepository  userDiseaseRepository;
    private final CloudinaryService cloudinaryService;// ← agrega

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

    @GetMapping("/onboarding/{userId}/needed")
    public ResponseEntity<Map<String, Boolean>> checkOnboarding(
            @PathVariable Long userId) {
        return ResponseEntity.ok(Map.of(
                "needed", userService.needsOnboarding(userId)
        ));
    }

    @GetMapping("/me/diseases/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getUserDiseases(
            @PathVariable Long userId) {
        List<Map<String, Object>> diseases = userDiseaseRepository
                .findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(ud -> Map.<String, Object>of(
                        "id",       ud.getDisease().getId(),
                        "name",     ud.getDisease().getName(),
                        "iconCode", ud.getDisease().getIconCode()
                ))
                .toList();
        return ResponseEntity.ok(diseases);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> updateProfile(
            @PathVariable Long userId,
            @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(userService.updateProfile(userId, req));
    }
    @PatchMapping("/{userId}/picture")
    public ResponseEntity<Map<String, String>> updatePicture(
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {
        String pictureUrl = body.get("pictureUrl");
        if (pictureUrl == null || pictureUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        userService.updatePicture(userId, pictureUrl);
        return ResponseEntity.ok(Map.of("pictureUrl", pictureUrl));
    }
    @GetMapping("/{userId}/upload-signature")
    public ResponseEntity<Map<String, Object>> getUploadSignature(@PathVariable Long userId) {
        // Definimos una carpeta organizada por ID de usuario en Cloudinary
        String folderPath = "nutrimed/users/" + userId;

        // Generamos la firma y la devolvemos
        Map<String, Object> signatureData = cloudinaryService.getSignature(folderPath);
        return ResponseEntity.ok(signatureData);
    }
}