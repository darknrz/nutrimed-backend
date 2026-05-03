package com.tuapp.nutrimed_backend.recipe.dto;

public record RecipeDto(
        Long    id,
        String  title,
        String  description,
        String  mealType,
        Integer prepMin,
        Integer cookMin,
        Double  kcal,
        Double  proteinG,
        Double  carbsG,
        Double  fatG,
        Double  sodiumMg,
        String  imageEmoji,
        String  difficulty,
        boolean isCompatible
) {}