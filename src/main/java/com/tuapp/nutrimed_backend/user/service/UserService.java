package com.tuapp.nutrimed_backend.user.service;

import com.tuapp.nutrimed_backend.disease.entity.Disease;
import com.tuapp.nutrimed_backend.disease.entity.UserDisease;
import com.tuapp.nutrimed_backend.disease.repository.DiseaseRepository;
import com.tuapp.nutrimed_backend.disease.repository.UserDiseaseRepository;
import com.tuapp.nutrimed_backend.user.dto.OnboardingRequest;
import com.tuapp.nutrimed_backend.user.entity.User;
import com.tuapp.nutrimed_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository       userRepository;
    private final DiseaseRepository    diseaseRepository;
    private final UserDiseaseRepository userDiseaseRepository;

    @Transactional
    public User completeOnboarding(Long userId, OnboardingRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar datos del perfil
        user.setCountryCode(req.countryCode());
        user.setBirthYear(req.birthYear());
        user.setSex(req.sex());
        user.setWeightKg(req.weightKg());
        user.setHeightCm(req.heightCm());
        user.setActivityLevel(req.activityLevel());
        user.setDietType(req.dietType());
        user.setHealthGoal(req.healthGoal());
        user.setOnboardingDone(true);

        // Guardar condiciones médicas
        if (req.diseaseIds() != null && !req.diseaseIds().isEmpty()) {
            userDiseaseRepository.deleteByUserId(userId);
            List<Disease> diseases = diseaseRepository
                    .findAllById(req.diseaseIds());
            diseases.forEach(disease ->
                    userDiseaseRepository.save(
                            UserDisease.builder()
                                    .user(user)
                                    .disease(disease)
                                    .isActive(true)
                                    .severity("moderado")
                                    .build()
                    )
            );
        }

        return userRepository.save(user);
    }

    public boolean needsOnboarding(Long userId) {
        return userRepository.findById(userId)
                .map(u -> !u.isOnboardingDone())
                .orElse(true);
    }
}