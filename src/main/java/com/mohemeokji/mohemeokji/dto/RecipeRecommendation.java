package com.mohemeokji.mohemeokji.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RecipeRecommendation {
    private String recipeName;
    private String description;
    private List<NeededIngredient> ingredients;

    @Getter
    public static class NeededIngredient {
        private String name;
        private Double quantityPerServing; // 1인분당 필요한 양
        private String unit;
    }
}