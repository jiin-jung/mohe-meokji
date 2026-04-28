package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Service
public class RecipeIngredientPolicyService {

    private static final Set<String> ALLOWED_UNITS = Set.of(
            "g", "kg",
            "ml", "L",
            "개", "알", "장", "줄기", "쪽", "모", "단",
            "큰술", "작은술", "컵"
    );

    private static final List<String> FORBIDDEN_EXPRESSIONS = List.of(
            "적당량", "약간", "조금", "한 줌", "한줌", "취향껏", "약간의"
    );

    public void normalizeAndValidateRecommendations(List<RecipeRecommendationDto> recipes) {
        if (recipes == null || recipes.isEmpty()) {
            throw new InvalidInputException("추천 레시피가 비어 있습니다.");
        }

        for (RecipeRecommendationDto recipe : recipes) {
            validateRecipe(recipe);
            recipe.setIngredients(recipe.getIngredients().stream()
                    .map(this::normalizeIngredient)
                    .toList());
        }
    }

    public void validateMissingIngredients(List<RecipeRecommendationDto.NeededIngredientDto> missingIngredients) {
        if (missingIngredients == null) {
            return;
        }

        for (RecipeRecommendationDto.NeededIngredientDto ingredient : missingIngredients) {
            validateName(ingredient.getName());
            validateQuantity(ingredient.getQuantity());
            validateUnit(ingredient.getUnit());
        }
    }

    private void validateRecipe(RecipeRecommendationDto recipe) {
        if (recipe == null || !StringUtils.hasText(recipe.getRecipeName())) {
            throw new InvalidInputException("레시피 이름이 비어 있습니다.");
        }
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            throw new InvalidInputException("레시피 재료가 비어 있습니다: " + recipe.getRecipeName());
        }
    }

    private RecipeRecommendationDto.IngredientDto normalizeIngredient(RecipeRecommendationDto.IngredientDto ingredient) {
        validateName(ingredient.getName());
        validateQuantity(ingredient.getQuantityPerServing());
        validateUnit(ingredient.getUnit());

        String normalizedUnit = ingredient.getUnit().trim();
        double normalizedQuantity = ingredient.getQuantityPerServing();

        if ("kg".equals(normalizedUnit)) {
            normalizedUnit = "g";
            normalizedQuantity = normalizedQuantity * 1000;
        } else if ("L".equals(normalizedUnit)) {
            normalizedUnit = "ml";
            normalizedQuantity = normalizedQuantity * 1000;
        }

        ingredient.setUnit(normalizedUnit);
        ingredient.setQuantityPerServing(normalizedQuantity);
        ingredient.setName(ingredient.getName().trim());
        return ingredient;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidInputException("재료명은 비어 있을 수 없습니다.");
        }

        String normalizedName = name.trim();
        for (String forbiddenExpression : FORBIDDEN_EXPRESSIONS) {
            if (normalizedName.contains(forbiddenExpression)) {
                throw new InvalidInputException("금지 표현이 포함된 재료명입니다: " + normalizedName);
            }
        }
    }

    private void validateQuantity(Double quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidInputException("재료 수량은 0보다 커야 합니다.");
        }
    }

    private void validateUnit(String unit) {
        if (!StringUtils.hasText(unit)) {
            throw new InvalidInputException("재료 단위는 비어 있을 수 없습니다.");
        }

        String normalizedUnit = unit.trim();
        if (!ALLOWED_UNITS.contains(normalizedUnit)) {
            throw new InvalidInputException("허용되지 않은 재료 단위입니다: " + normalizedUnit);
        }
    }
}