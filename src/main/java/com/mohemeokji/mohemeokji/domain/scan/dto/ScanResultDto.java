package com.mohemeokji.mohemeokji.domain.scan.dto;

import java.util.List;

public record ScanResultDto(List<ScanIngredientDto> detectedIngredients) {}