package com.mohemeokji.mohemeokji.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRecommendationDto {
    @JsonAlias("menuName")
    private String recipeName;
    private Long id; // 저장된 레시피 조회를 위한 ID
    private String description;
    private String ytSearchQuery; // 제미나이가 정해주는 유튜브 검색어
    private String youtubeUrl;    // 백엔드에서 유튜브 API로 채울 필드
    private List<IngredientDto> ingredients;
    private int maxServings;
    private List<String> steps;
    private String category; // '육류', '해산물', '채소' 등 분류 (Gemini가 반환)

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientDto {
        private String name;
        private Double quantityPerServing;
        private String unit;
        private String category; // '육류', '해산물', '조미료' 등 카테고리 정보
    }
}