package com.tuapp.nutrimed_backend.user.service;

import com.tuapp.nutrimed_backend.disease.dto.DiseaseDto;
import com.tuapp.nutrimed_backend.disease.entity.Disease;
import com.tuapp.nutrimed_backend.disease.entity.UserDisease;
import com.tuapp.nutrimed_backend.disease.repository.DiseaseRepository;
import com.tuapp.nutrimed_backend.disease.repository.UserDiseaseRepository;
import com.tuapp.nutrimed_backend.user.dto.OnboardingRequest;
import com.tuapp.nutrimed_backend.user.dto.UpdateProfileRequest;
import com.tuapp.nutrimed_backend.user.dto.UserProfileDto;
import com.tuapp.nutrimed_backend.user.entity.User;
import com.tuapp.nutrimed_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository        userRepository;
    private final DiseaseRepository     diseaseRepository;
    private final UserDiseaseRepository userDiseaseRepository;

    // ── ONBOARDING ────────────────────────────────────────────
    @Transactional
    public User completeOnboarding(Long userId, OnboardingRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setCountryCode(req.countryCode());
        user.setBirthYear(req.birthYear());
        user.setSex(req.sex());
        user.setWeightKg(req.weightKg());
        user.setHeightCm(req.heightCm());
        user.setActivityLevel(req.activityLevel());
        user.setDietType(req.dietType());
        user.setHealthGoal(req.healthGoal());
        user.setOnboardingDone(true);

        if (req.diseaseIds() != null && !req.diseaseIds().isEmpty()) {
            userDiseaseRepository.deleteByUserId(userId);
            diseaseRepository.findAllById(req.diseaseIds())
                    .forEach(disease -> userDiseaseRepository.save(
                            UserDisease.builder()
                                    .user(user)
                                    .disease(disease)
                                    .isActive(true)
                                    .severity("moderado")
                                    .build()
                    ));
        }

        return userRepository.save(user);
    }

    public boolean needsOnboarding(Long userId) {
        return userRepository.findById(userId)
                .map(u -> !u.isOnboardingDone())
                .orElse(true);
    }

    // ── PERFIL ────────────────────────────────────────────────
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<DiseaseDto> diseases = userDiseaseRepository
                .findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(ud -> new DiseaseDto(
                        ud.getDisease().getId(),
                        ud.getDisease().getName(),
                        ud.getDisease().getSlug(),
                        ud.getDisease().getDescription(),
                        ud.getDisease().getCategory(),
                        ud.getDisease().getIconCode()
                ))
                .toList();

        return new UserProfileDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPicture(),
                user.getCountryCode(),
                user.getSex(),
                user.getBirthYear(),
                user.getWeightKg(),
                user.getHeightCm(),
                user.getActivityLevel(),
                user.getHealthGoal(),
                diseases
        );
    }

    // ── ACTUALIZAR PERFIL ─────────────────────────────────────
    @Transactional
    public UserProfileDto updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (req.name()          != null) user.setName(req.name());
        if (req.birthYear()     != null) user.setBirthYear(req.birthYear());
        if (req.sex()           != null) user.setSex(req.sex());
        if (req.weightKg()      != null) user.setWeightKg(req.weightKg());
        if (req.heightCm()      != null) user.setHeightCm(req.heightCm());
        if (req.activityLevel() != null) user.setActivityLevel(req.activityLevel());
        if (req.healthGoal()    != null) user.setHealthGoal(req.healthGoal());

        if (req.diseaseIds() != null) {
            userDiseaseRepository.deleteByUserId(userId);
            if (!req.diseaseIds().isEmpty()) {
                diseaseRepository.findAllById(req.diseaseIds())
                        .forEach(disease -> userDiseaseRepository.save(
                                UserDisease.builder()
                                        .user(user)
                                        .disease(disease)
                                        .isActive(true)
                                        .severity("moderado")
                                        .build()
                        ));
            }
        }

        userRepository.save(user);
        return getUserProfile(userId);
    }
    @Transactional
    public void updatePicture(Long userId, String pictureUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setPicture(pictureUrl);
        userRepository.save(user);
    }
}