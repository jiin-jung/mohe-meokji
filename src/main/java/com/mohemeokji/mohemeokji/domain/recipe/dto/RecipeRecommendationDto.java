package com.mohemeokji.mohemeokji.domain.recipe.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRecommendationDto {
    @JsonAlias("menuName")
    @NotBlank
    private String recipeName;
    private Long id;
    private String description;
    private String ytSearchQuery;
    private String youtubeUrl;
    @Valid
    @NotEmpty
    private List<IngredientDto> ingredients;
    private int maxServings;
    @NotEmpty
    private List<String> steps;
    private String category;
    private List<NeededIngredientDto> missingIngredients;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDto {
        @NotBlank
        private String name;
        @NotNull
        @Positive
        @JsonAlias("quantity")
        private Double quantityPerServing;
        @NotBlank
        private String unit;
        @NotBlank
        private String category;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NeededIngredientDto {
        private String name;
        private Double quantity;
        private String unit;
        private String category;
    }
}