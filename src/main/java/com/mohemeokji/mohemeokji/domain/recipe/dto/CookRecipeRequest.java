package com.mohemeokji.mohemeokji.domain.recipe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CookRecipeRequest {
    @Valid
    @NotNull
    private RecipeRecommendationDto recipe;

    @Min(1)
    private int servings = 1;
}