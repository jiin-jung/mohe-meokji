package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.SavedRecipe;
import com.mohemeokji.mohemeokji.domain.SavedRecipeIngredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.repository.SavedRecipeRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveRecipe(Long userId, RecipeRecommendationDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        SavedRecipe savedRecipe = SavedRecipe.builder()
                .user(user)
                .recipeName(dto.getRecipeName())
                .description(dto.getDescription())
                .youtubeUrl(dto.getYoutubeUrl())
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
                .youtubeUrl(recipe.getYoutubeUrl())
                .category(recipe.getCategory())
                .steps(recipe.getSteps())
                .ingredients(ingredientDtos)
                // maxServings는 저장된 데이터에는 없으므로 기본값 0 또는 필요 시 재계산 로직 추가 가능
                .build();
    }
    @Transactional
    public void deleteSavedRecipe(Long recipeId) {
        savedRecipeRepository.deleteById(recipeId);
    }
}
