package com.tuapp.nutrimed_backend.recipe.service;

import com.tuapp.nutrimed_backend.disease.repository.UserDiseaseRepository;
import com.tuapp.nutrimed_backend.recipe.dto.RecipeDto;
import com.tuapp.nutrimed_backend.recipe.entity.Recipe;
import com.tuapp.nutrimed_backend.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository      recipeRepository;
    private final UserDiseaseRepository userDiseaseRepository;

    public List<RecipeDto> getRecommended(Long userId) {
        // Obtener IDs de enfermedades activas del usuario
        List<Long> diseaseIds = userDiseaseRepository
                .findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(ud -> ud.getDisease().getId())
                .toList();

        List<Recipe> recipes;
        if (diseaseIds.isEmpty()) {
            // Sin condiciones → todas las recetas
            recipes = recipeRepository.findByIsActiveTrueOrderByIdAsc();
        } else {
            // Con condiciones → filtrar incompatibles
            recipes = recipeRepository.findCompatibleRecipes(diseaseIds);
        }

        return recipes.stream()
                .map(r -> new RecipeDto(
                        r.getId(), r.getTitle(), r.getDescription(),
                        r.getMealType(), r.getPrepMin(), r.getCookMin(),
                        r.getKcal(), r.getProteinG(), r.getCarbsG(),
                        r.getFatG(), r.getSodiumMg(), r.getImageEmoji(),
                        r.getDifficulty(), true
                ))
                .toList();
    }

    public List<RecipeDto> getByMealType(Long userId, String mealType) {
        return getRecommended(userId).stream()
                .filter(r -> r.mealType().equals(mealType))
                .toList();
    }

    public List<RecipeDto> getAll() {
        return recipeRepository.findByIsActiveTrueOrderByIdAsc()
                .stream()
                .map(r -> new RecipeDto(
                        r.getId(), r.getTitle(), r.getDescription(),
                        r.getMealType(), r.getPrepMin(), r.getCookMin(),
                        r.getKcal(), r.getProteinG(), r.getCarbsG(),
                        r.getFatG(), r.getSodiumMg(), r.getImageEmoji(),
                        r.getDifficulty(), true
                ))
                .toList();
    }
}