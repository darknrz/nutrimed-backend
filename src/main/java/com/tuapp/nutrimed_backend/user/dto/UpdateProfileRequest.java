package com.tuapp.nutrimed_backend.user.dto;

import java.util.List;

public record UpdateProfileRequest(
        String      name,
        Integer     birthYear,
        String      sex,
        Double      weightKg,
        Double      heightCm,
        String      activityLevel,
        String      healthGoal,
        List<Long>  diseaseIds
) {}