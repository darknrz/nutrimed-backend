package com.tuapp.nutrimed_backend.disease.entity;

import com.tuapp.nutrimed_backend.ingredient.entity.Ingredient;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "disease_restrictions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class DiseaseRestriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "restriction_type")
    private String restrictionType;

    private String reason;

    @Column(name = "max_daily_g")
    private Double maxDailyG;
}