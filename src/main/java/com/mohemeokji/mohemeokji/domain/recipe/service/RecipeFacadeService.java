package com.mohemeokji.mohemeokji.domain.recipe.service;

import com.mohemeokji.mohemeokji.domain.disliked.service.DislikedRecipeService;
import com.mohemeokji.mohemeokji.domain.ingredient.Ingredient;
import com.mohemeokji.mohemeokji.domain.ingredient.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.user.dto.UserSettingsDto;
import com.mohemeokji.mohemeokji.domain.user.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeFacadeService {

    private final IngredientRepository ingredientRepository;
    private final GeminiService geminiService;
    private final YouTubeService youtubeService;
    private final CookService cookService;
    private final DislikedRecipeService dislikedRecipeService;
    private final UserSettingsService userSettingsService;

    public List<RecipeRecommendationDto> recommendRecipes(Long userId) {
        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);
        UserSettingsDto settings = userSettingsService.getSettings(userId);
        List<RecipeRecommendationDto> recipes = geminiService.getRecipeRecommendations(myIngredients, settings);
        Set<String> dislikedRecipeNames = dislikedRecipeService.getDislikedRecipeNames(userId);

        List<RecipeRecommendationDto> filteredRecipes = new ArrayList<>();
        for (RecipeRecommendationDto recipe : recipes) {
            if (dislikedRecipeNames.contains(normalizeRecipeName(recipe.getRecipeName()))) continue;
            String searchQuery = youtubeService.buildSearchQuery(recipe.getRecipeName(), recipe.getYtSearchQuery());
            recipe.setYtSearchQuery(searchQuery);
            recipe.setYoutubeUrl(youtubeService.resolveRecipeVideoUrl(recipe.getRecipeName(), searchQuery));
            recipe.setMaxServings(cookService.calculateMaxServings(userId, recipe));
            recipe.setMissingIngredients(calculateMissingIngredients(myIngredients, recipe));
            filteredRecipes.add(recipe);
        }
        return filteredRecipes;
    }

    private List<RecipeRecommendationDto.NeededIngredientDto> calculateMissingIngredients(List<Ingredient> myIngredients, RecipeRecommendationDto recipe) {
        List<RecipeRecommendationDto.NeededIngredientDto> missing = new ArrayList<>();
        if (recipe.getIngredients() == null) return missing;
        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            Ingredient stock = myIngredients.stream().filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName())).findFirst().orElse(null);
            double stockQty = stock == null || stock.getQuantity() == null ? 0.0 : stock.getQuantity();
            double requiredQty = required.getQuantityPerServing() == null ? 0.0 : required.getQuantityPerServing();
            if (requiredQty - stockQty > 0) missing.add(new RecipeRecommendationDto.NeededIngredientDto(required.getName(), requiredQty - stockQty, required.getUnit(), required.getCategory()));
        }
        return missing;
    }

    private String normalizeRecipeName(String recipeName) {
        if (recipeName == null) return "";
        return recipeName.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }
}