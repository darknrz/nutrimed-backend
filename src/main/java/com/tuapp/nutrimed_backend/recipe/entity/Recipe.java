package com.tuapp.nutrimed_backend.recipe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recipes")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "meal_type")
    private String mealType;

    @Column(name = "prep_min")
    private Integer prepMin;

    @Column(name = "cook_min")
    private Integer cookMin;

    private Integer servings;
    private Double kcal;

    @Column(name = "protein_g")
    private Double proteinG;

    @Column(name = "carbs_g")
    private Double carbsG;

    @Column(name = "fat_g")
    private Double fatG;

    @Column(name = "fiber_g")
    private Double fiberG;

    @Column(name = "sodium_mg")
    private Double sodiumMg;

    @Column(name = "image_emoji")
    private String imageEmoji;

    private String difficulty;

    @Column(name = "origin_country")
    private String originCountry;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}