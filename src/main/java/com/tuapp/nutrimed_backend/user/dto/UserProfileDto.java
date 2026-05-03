package com.tuapp.nutrimed_backend.user.dto;

import com.tuapp.nutrimed_backend.disease.dto.DiseaseDto;
import java.util.List;

public record UserProfileDto(
        Long         id,
        String       name,
        String       email,
        String       countryCode,
        String       sex,
        Integer      birthYear,
        Double       weightKg,
        Double       heightCm,
        String       activityLevel,
        String       healthGoal,
        List<DiseaseDto> diseases
) {}