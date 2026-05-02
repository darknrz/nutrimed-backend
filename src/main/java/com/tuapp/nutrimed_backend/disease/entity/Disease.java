package com.tuapp.nutrimed_backend.disease.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diseases")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String slug;
    private String description;
    private String category;

    @Column(name = "icon_code")
    private String iconCode;

    @Column(name = "is_active")
    private boolean isActive;
}