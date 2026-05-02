package com.tuapp.nutrimed_backend.disease.dto;

public record DiseaseDto(
        Long   id,
        String name,
        String slug,
        String description,
        String category,
        String iconCode
) {}