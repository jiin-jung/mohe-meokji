package com.mohemeokji.mohemeokji.dto;

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
    private Long id; // 저장된 레시피 조회를 위한 ID
    private String description;
    private String ytSearchQuery; // 제미나이가 정해주는 유튜브 검색어
    private String youtubeUrl;    // 백엔드에서 유튜브 API로 채울 필드
    @Valid
    @NotEmpty
    private List<IngredientDto> ingredients;
    private int maxServings;
    @NotEmpty
    private List<String> steps;
    private String category; // '육류', '해산물', '채소' 등 분류 (Gemini가 반환)
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
        private String category; // '육류', '해산물', '조미료' 등 카테고리 정보
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
