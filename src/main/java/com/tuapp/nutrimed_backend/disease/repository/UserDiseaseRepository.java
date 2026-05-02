package com.tuapp.nutrimed_backend.disease.repository;

import com.tuapp.nutrimed_backend.disease.entity.UserDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserDiseaseRepository extends JpaRepository<UserDisease, Long> {
    List<UserDisease> findByUserIdAndIsActiveTrue(Long userId);
    void deleteByUserId(Long userId);
}