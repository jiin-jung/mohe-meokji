package com.mohemeokji.mohemeokji.domain.disliked.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DislikedRecipeRequest {
    @NotBlank
    private String recipeName;

    private String category;
}