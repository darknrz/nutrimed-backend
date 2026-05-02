package com.tuapp.nutrimed_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;        // nick visible en la app

    private String name;            // nombre real (opcional)
    private String picture;         // foto de perfil

    private String password;        // null si es Google

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @Column(name = "onboarding_done")
    private boolean onboardingDone = false;

    @Column(name = "country_code")
    private String countryCode = "PE";

    @Column(name = "birth_year")
    private Integer birthYear;

    private String sex;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "height_cm")
    private Double heightCm;

    @Column(name = "activity_level")
    private String activityLevel = "moderado";

    @Column(name = "diet_type")
    private String dietType = "omnivoro";

    @Column(name = "health_goal")
    private String healthGoal = "controlar_enfermedad";

    public enum AuthProvider { GOOGLE, EMAIL }
}