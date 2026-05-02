package com.tuapp.nutrimed_backend.disease.entity;

import com.tuapp.nutrimed_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_diseases")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @Column(name = "since_year")
    private Integer sinceYear;

    @Column(name = "is_active")
    private boolean isActive = true;

    private String severity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}