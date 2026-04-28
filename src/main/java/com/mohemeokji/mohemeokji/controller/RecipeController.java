package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.dto.ApiMessageResponse;
import com.mohemeokji.mohemeokji.dto.CookRecipeRequest;
import com.mohemeokji.mohemeokji.dto.DislikedRecipeRequest;
import com.mohemeokji.mohemeokji.dto.DislikedRecipeResponse;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.service.CookService;
import com.mohemeokji.mohemeokji.service.DislikedRecipeService;
import com.mohemeokji.mohemeokji.service.RecipeFacadeService;
import com.mohemeokji.mohemeokji.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@CrossOrigin(origins = "http://localhost:5173")
public class RecipeController {

    private final RecipeFacadeService recipeFacadeService;
    private final CookService cookService;
    private final RecipeService recipeService;
    private final DislikedRecipeService dislikedRecipeService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/recommendations/me")
    public List<RecipeRecommendationDto> recommend() {
        return recipeFacadeService.recommendRecipes(currentUserProvider.getCurrentUserId());
    }

    @PostMapping("/cook/me")
    public ApiMessageResponse completeCooking(@Valid @RequestBody CookRecipeRequest request) {
        cookService.cook(currentUserProvider.getCurrentUserId(), request.getRecipe(), request.getServings());
        return new ApiMessageResponse(request.getServings() + "인분 요리가 완료되어 재고가 차감되었습니다.");
    }

    @PostMapping("/saved/me")
    public ApiMessageResponse saveRecipe(@Valid @RequestBody RecipeRecommendationDto recipe) {
        recipeService.saveRecipe(currentUserProvider.getCurrentUserId(), recipe);
        return new ApiMessageResponse("레시피가 저장되었습니다.");
    }

    @GetMapping("/saved/me")
    public List<RecipeRecommendationDto> getSavedRecipes() {
        return recipeService.getSavedRecipes(currentUserProvider.getCurrentUserId());
    }

    @DeleteMapping("/saved/{recipeId}")
    public ApiMessageResponse deleteSavedRecipe(@PathVariable Long recipeId) {
        recipeService.deleteSavedRecipe(recipeId);
        return new ApiMessageResponse("레시피가 삭제되었습니다.");
    }

    @PostMapping("/dislikes/me")
    public ApiMessageResponse dislikeRecipe(@Valid @RequestBody DislikedRecipeRequest request) {
        dislikedRecipeService.addDislikedRecipe(currentUserProvider.getCurrentUserId(), request);
        return new ApiMessageResponse("레시피가 싫어요 목록에 추가되었습니다.");
    }

    @GetMapping("/dislikes/me")
    public List<DislikedRecipeResponse> getDislikedRecipes() {
        return dislikedRecipeService.getDislikedRecipes(currentUserProvider.getCurrentUserId());
    }

    @DeleteMapping("/dislikes/{dislikeId}")
    public ApiMessageResponse deleteDislikedRecipe(@PathVariable Long dislikeId) {
        dislikedRecipeService.deleteDislikedRecipe(dislikeId);
        return new ApiMessageResponse("싫어요 레시피가 해제되었습니다.");
    }
}
