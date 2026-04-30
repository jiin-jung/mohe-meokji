package com.mohemeokji.mohemeokji.domain.recipe.service;

import com.mohemeokji.mohemeokji.domain.recipe.SavedRecipe;
import com.mohemeokji.mohemeokji.domain.recipe.SavedRecipeIngredient;
import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeErrorCode;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeException;
import com.mohemeokji.mohemeokji.domain.recipe.repository.SavedRecipeRepository;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;
    private final YouTubeService youtubeService;
    private final RecipeIngredientPolicyService recipeIngredientPolicyService;

    @Transactional
    public Long saveRecipe(Long userId, RecipeRecommendationDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
        recipeIngredientPolicyService.normalizeAndValidateRecommendations(List.of(dto));
        String normalizedRecipeName = normalizeRecipeName(dto.getRecipeName());
        if (!StringUtils.hasText(normalizedRecipeName)) throw new RecipeException(RecipeErrorCode.INVALID_RECIPE_NAME);
        if (savedRecipeRepository.existsByUserIdAndNormalizedRecipeName(userId, normalizedRecipeName)) throw new RecipeException(RecipeErrorCode.DUPLICATE_SAVED_RECIPE);
        String searchQuery = youtubeService.buildSearchQuery(dto.getRecipeName(), dto.getYtSearchQuery());
        SavedRecipe savedRecipe = SavedRecipe.builder().user(user).recipeName(dto.getRecipeName()).normalizedRecipeName(normalizedRecipeName)
                .description(dto.getDescription()).ytSearchQuery(searchQuery).youtubeUrl(youtubeService.resolveRecipeVideoUrl(dto.getRecipeName(), searchQuery))
                .category(dto.getCategory()).steps(dto.getSteps()).build();
        if (dto.getIngredients() != null) {
            for (RecipeRecommendationDto.IngredientDto ing : dto.getIngredients()) {
                savedRecipe.addIngredient(SavedRecipeIngredient.builder().name(ing.getName()).quantityPerServing(ing.getQuantityPerServing()).unit(ing.getUnit()).category(ing.getCategory()).build());
            }
        }
        return savedRecipeRepository.save(savedRecipe).getId();
    }

    public List<RecipeRecommendationDto> getSavedRecipes(Long userId) {
        return savedRecipeRepository.findByUserId(userId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteSavedRecipe(Long recipeId) {
        if (!savedRecipeRepository.existsById(recipeId)) throw new RecipeException(RecipeErrorCode.RECIPE_NOT_FOUND, "저장된 레시피가 없습니다. id=" + recipeId);
        savedRecipeRepository.deleteById(recipeId);
    }

    private RecipeRecommendationDto convertToDto(SavedRecipe recipe) {
        List<RecipeRecommendationDto.IngredientDto> ingredientDtos = recipe.getIngredients().stream()
                .map(i -> new RecipeRecommendationDto.IngredientDto(i.getName(), i.getQuantityPerServing(), i.getUnit(), i.getCategory())).collect(Collectors.toList());
        return RecipeRecommendationDto.builder().id(recipe.getId()).recipeName(recipe.getRecipeName()).description(recipe.getDescription())
                .ytSearchQuery(recipe.getYtSearchQuery()).youtubeUrl(recipe.getYoutubeUrl()).category(recipe.getCategory()).steps(recipe.getSteps()).ingredients(ingredientDtos).build();
    }

    private String normalizeRecipeName(String recipeName) {
        if (!StringUtils.hasText(recipeName)) return null;
        return recipeName.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }
}