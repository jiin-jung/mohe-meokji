package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.service.CookService;
import com.mohemeokji.mohemeokji.service.GeminiService;
import com.mohemeokji.mohemeokji.service.RecipeService;
import com.mohemeokji.mohemeokji.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "http://localhost:5173")
public class RecipeController {

    private final GeminiService geminiService;
    private final IngredientRepository ingredientRepository;
    private final CookService cookService;
    private final YouTubeService youtubeService;
    private final RecipeService recipeService;

    @GetMapping("/recommend/{userId}")
    public List<RecipeRecommendationDto> recommend(@PathVariable Long userId) {
        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);

        // 1. 제미나이 추천 (List 반환)
        List<RecipeRecommendationDto> recipes = geminiService.getRecipeRecommendations(myIngredients);

        for (RecipeRecommendationDto recipe : recipes) {
            // 2. 각 레시피의 검색어로 유튜브 URL 낚아채기
            String videoUrl = youtubeService.getTopVideoUrl(recipe.getYtSearchQuery());
            recipe.setYoutubeUrl(videoUrl);

            // 3. 인분 계산기 가동
            recipe.setMaxServings(cookService.calculateMaxServings(userId, recipe));
        }

        return recipes;
    }

    @PostMapping("/cook/{userId}")
    public String completeCooking(
            @PathVariable Long userId,
            @RequestBody RecipeRecommendationDto recipe, // 추천받았던 레시피 데이터 그대로 전달
            @RequestParam(defaultValue = "1") int servings) {

        cookService.cook(userId, recipe, servings);
        return servings + "인분 요리가 완료되어 재고가 차감되었습니다.";
    }

    @PostMapping("/save/{userId}")
    public String saveRecipe(@PathVariable Long userId, @RequestBody RecipeRecommendationDto recipe) {
        recipeService.saveRecipe(userId, recipe);
        return "레시피가 저장되었습니다.";
    }

    @GetMapping("/saved/{userId}")
    public List<RecipeRecommendationDto> getSavedRecipes(@PathVariable Long userId) {
        return recipeService.getSavedRecipes(userId);
    }

    @DeleteMapping("/saved/{recipeId}")
    public String deleteSavedRecipe(@PathVariable Long recipeId) {
        recipeService.deleteSavedRecipe(recipeId);
        return "레시피가 삭제되었습니다.";
    }
}