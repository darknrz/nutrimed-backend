package com.tuapp.nutrimed_backend.ingredient.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredients")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String unit;

    @Column(name = "kcal_per_100g")
    private Double kcalPer100g;

    @Column(name = "glycemic_index")
    private Integer glycemicIndex;

    @Column(name = "sodium_mg")
    private Double sodiumMg;

    @Column(name = "is_allergen")
    private boolean isAllergen;

    @Column(name = "is_active")
    private boolean isActive;
}