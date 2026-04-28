package com.mohemeokji.mohemeokji.domain.imageanalyze.dto;

public record DetectedIngredientDto(
        String name,
        String category,
        String estimatedQuantity,
        String unit
) {}