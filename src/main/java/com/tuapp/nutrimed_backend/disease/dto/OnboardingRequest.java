package com.tuapp.nutrimed_backend.user.dto;

import java.util.List;

public record OnboardingRequest(
        String      countryCode,
        Integer     birthYear,
        String      sex,
        Double      weightKg,
        Double      heightCm,
        String      activityLevel,
        String      dietType,
        String      healthGoal,
        List<Long>  diseaseIds
) {}