package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.SavedRecipe;
import com.mohemeokji.mohemeokji.domain.SavedRecipeIngredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.exception.DuplicateResourceException;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import com.mohemeokji.mohemeokji.repository.SavedRecipeRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        recipeIngredientPolicyService.normalizeAndValidateRecommendations(List.of(dto));

        String normalizedRecipeName = normalizeRecipeName(dto.getRecipeName());
        if (!StringUtils.hasText(normalizedRecipeName)) {
            throw new InvalidInputException("레시피 이름이 올바르지 않습니다.");
        }
        if (savedRecipeRepository.existsByUserIdAndNormalizedRecipeName(userId, normalizedRecipeName)) {
            throw new DuplicateResourceException("이미 저장된 레시피입니다.");
        }

        String normalizedSearchQuery = youtubeService.buildSearchQuery(dto.getRecipeName(), dto.getYtSearchQuery());
        String resolvedYoutubeUrl = youtubeService.resolveRecipeVideoUrl(dto.getRecipeName(), normalizedSearchQuery);

        SavedRecipe savedRecipe = SavedRecipe.builder()
                .user(user)
                .recipeName(dto.getRecipeName())
                .normalizedRecipeName(normalizedRecipeName)
                .description(dto.getDescription())
                .ytSearchQuery(normalizedSearchQuery)
                .youtubeUrl(resolvedYoutubeUrl)
                .category(dto.getCategory())
                .steps(dto.getSteps())
                .build();

        if (dto.getIngredients() != null) {
            for (RecipeRecommendationDto.IngredientDto ingredientDto : dto.getIngredients()) {
                SavedRecipeIngredient ingredient = SavedRecipeIngredient.builder()
                        .name(ingredientDto.getName())
                        .quantityPerServing(ingredientDto.getQuantityPerServing())
                        .unit(ingredientDto.getUnit())
                        .category(ingredientDto.getCategory())
                        .build();
                savedRecipe.addIngredient(ingredient);
            }
        }

        return savedRecipeRepository.save(savedRecipe).getId();
    }

    public List<RecipeRecommendationDto> getSavedRecipes(Long userId) {
        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUserId(userId);
        return savedRecipes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private RecipeRecommendationDto convertToDto(SavedRecipe recipe) {
        List<RecipeRecommendationDto.IngredientDto> ingredientDtos = recipe.getIngredients().stream()
                .map(i -> new RecipeRecommendationDto.IngredientDto(
                        i.getName(),
                        i.getQuantityPerServing(),
                        i.getUnit(),
                        i.getCategory()
                ))
                .collect(Collectors.toList());

        return RecipeRecommendationDto.builder()
                .id(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .description(recipe.getDescription())
                .ytSearchQuery(recipe.getYtSearchQuery())
                .youtubeUrl(recipe.getYoutubeUrl())
                .category(recipe.getCategory())
                .steps(recipe.getSteps())
                .ingredients(ingredientDtos)
                .build();
    }

    @Transactional
    public void deleteSavedRecipe(Long recipeId) {
        if (!savedRecipeRepository.existsById(recipeId)) {
            throw new EntityNotFoundException("저장된 레시피가 없습니다. id=" + recipeId);
        }
        savedRecipeRepository.deleteById(recipeId);
    }

    private String normalizeRecipeName(String recipeName) {
        if (!StringUtils.hasText(recipeName)) {
            return null;
        }
        return recipeName.replaceAll("\\s+", "")
                .toLowerCase(Locale.ROOT);
    }
}