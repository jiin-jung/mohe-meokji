package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
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

    public List<RecipeRecommendationDto> recommendRecipes(Long userId) {
        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);
        List<RecipeRecommendationDto> recipes = geminiService.getRecipeRecommendations(myIngredients);
        Set<String> dislikedRecipeNames = dislikedRecipeService.getDislikedRecipeNames(userId);

        List<RecipeRecommendationDto> filteredRecipes = new ArrayList<>();
        for (RecipeRecommendationDto recipe : recipes) {
            if (dislikedRecipeNames.contains(normalizeRecipeName(recipe.getRecipeName()))) {
                continue;
            }

            String normalizedSearchQuery = youtubeService.buildSearchQuery(recipe.getRecipeName(), recipe.getYtSearchQuery());
            recipe.setYtSearchQuery(normalizedSearchQuery);
            recipe.setYoutubeUrl(youtubeService.resolveRecipeVideoUrl(recipe.getRecipeName(), normalizedSearchQuery));
            recipe.setMaxServings(cookService.calculateMaxServings(userId, recipe));
            recipe.setMissingIngredients(calculateMissingIngredients(myIngredients, recipe));
            filteredRecipes.add(recipe);
        }

        return filteredRecipes;
    }

    private List<RecipeRecommendationDto.NeededIngredientDto> calculateMissingIngredients(List<Ingredient> myIngredients,
                                                                                           RecipeRecommendationDto recipe) {
        List<RecipeRecommendationDto.NeededIngredientDto> missingIngredients = new ArrayList<>();
        if (recipe.getIngredients() == null) {
            return missingIngredients;
        }

        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            Ingredient stock = myIngredients.stream()
                    .filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName()))
                    .findFirst()
                    .orElse(null);

            double stockQuantity = stock == null || stock.getQuantity() == null ? 0.0 : stock.getQuantity();
            double requiredQuantity = required.getQuantityPerServing() == null ? 0.0 : required.getQuantityPerServing();
            double missingQuantity = requiredQuantity - stockQuantity;

            if (missingQuantity > 0) {
                missingIngredients.add(new RecipeRecommendationDto.NeededIngredientDto(
                        required.getName(),
                        missingQuantity,
                        required.getUnit(),
                        required.getCategory()
                ));
            }
        }

        return missingIngredients;
    }

    private String normalizeRecipeName(String recipeName) {
        if (recipeName == null) {
            return "";
        }
        return recipeName.replaceAll("\\s+", "")
                .toLowerCase(Locale.ROOT);
    }
}
