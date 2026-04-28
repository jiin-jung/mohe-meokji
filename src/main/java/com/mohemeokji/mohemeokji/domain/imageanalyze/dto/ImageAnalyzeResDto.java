package com.mohemeokji.mohemeokji.domain.imageanalyze.dto;

import java.util.List;

public record ImageAnalyzeResDto(
        List<DetectedIngredientDto> detectedIngredients
) {}