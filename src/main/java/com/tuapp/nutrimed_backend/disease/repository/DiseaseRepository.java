package com.tuapp.nutrimed_backend.disease.repository;

import com.tuapp.nutrimed_backend.disease.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByIsActiveTrue();
    List<Disease> findByCategory(String category);
}