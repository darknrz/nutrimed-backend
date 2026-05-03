package com.tuapp.nutrimed_backend.recipe.controller;

import com.tuapp.nutrimed_backend.recipe.dto.RecipeDto;
import com.tuapp.nutrimed_backend.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    // Todas las recetas
    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAll() {
        return ResponseEntity.ok(recipeService.getAll());
    }

    // Recetas recomendadas para el usuario
    @GetMapping("/recommended/{userId}")
    public ResponseEntity<List<RecipeDto>> getRecommended(
            @PathVariable Long userId) {
        return ResponseEntity.ok(recipeService.getRecommended(userId));
    }

    // Por tipo de comida
    @GetMapping("/recommended/{userId}/{mealType}")
    public ResponseEntity<List<RecipeDto>> getByMealType(
            @PathVariable Long userId,
            @PathVariable String mealType) {
        return ResponseEntity.ok(
                recipeService.getByMealType(userId, mealType));
    }
}