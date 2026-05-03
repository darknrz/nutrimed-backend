package com.tuapp.nutrimed_backend.recipe.repository;

import com.tuapp.nutrimed_backend.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByIsActiveTrueOrderByIdAsc();

    List<Recipe> findByMealTypeAndIsActiveTrue(String mealType);

    // Recetas que NO contienen ingredientes AVOID para las enfermedades del usuario
    @Query("""
        SELECT DISTINCT r FROM Recipe r
        WHERE r.isActive = true
        AND r.id NOT IN (
            SELECT DISTINCT ri.recipe.id
            FROM RecipeIngredient ri
            WHERE ri.ingredient.id IN (
                SELECT dr.ingredient.id
                FROM DiseaseRestriction dr
                WHERE dr.disease.id IN :diseaseIds
                AND dr.restrictionType = 'AVOID'
            )
        )
        ORDER BY r.id ASC
    """)
    List<Recipe> findCompatibleRecipes(@Param("diseaseIds") List<Long> diseaseIds);
}