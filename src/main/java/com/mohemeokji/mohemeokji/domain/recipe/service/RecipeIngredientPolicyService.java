package com.mohemeokji.mohemeokji.domain.recipe.service;

import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeErrorCode;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

@Service
public class RecipeIngredientPolicyService {

    private static final Set<String> ALLOWED_UNITS = Set.of(
            "g", "kg", "ml", "L", "개", "알", "장", "줄기", "쪽", "모", "단", "큰술", "작은술", "컵");

    private static final List<String> FORBIDDEN_EXPRESSIONS = List.of(
            "적당량", "약간", "조금", "한 줌", "한줌", "취향껏", "약간의");

    public void normalizeAndValidateRecommendations(List<RecipeRecommendationDto> recipes) {
        if (recipes == null || recipes.isEmpty()) throw new RecipeException(RecipeErrorCode.EMPTY_RECIPE_LIST);
        for (RecipeRecommendationDto recipe : recipes) {
            validateRecipe(recipe);
            recipe.setIngredients(recipe.getIngredients().stream().map(this::normalizeIngredient).toList());
        }
    }

    public void validateMissingIngredients(List<RecipeRecommendationDto.NeededIngredientDto> missingIngredients) {
        if (missingIngredients == null) return;
        for (RecipeRecommendationDto.NeededIngredientDto ingredient : missingIngredients) {
            validateName(ingredient.getName());
            validateQuantity(ingredient.getQuantity());
            validateUnit(ingredient.getUnit());
        }
    }

    private void validateRecipe(RecipeRecommendationDto recipe) {
        if (recipe == null || !StringUtils.hasText(recipe.getRecipeName())) throw new RecipeException(RecipeErrorCode.EMPTY_RECIPE_NAME);
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty())
            throw new RecipeException(RecipeErrorCode.EMPTY_RECIPE_INGREDIENTS, "레시피 재료가 비어 있습니다: " + recipe.getRecipeName());
    }

    private RecipeRecommendationDto.IngredientDto normalizeIngredient(RecipeRecommendationDto.IngredientDto ingredient) {
        validateName(ingredient.getName());
        validateQuantity(ingredient.getQuantityPerServing());
        validateUnit(ingredient.getUnit());
        String normalizedUnit = ingredient.getUnit().trim();
        double normalizedQuantity = ingredient.getQuantityPerServing();
        if ("kg".equals(normalizedUnit)) { normalizedUnit = "g"; normalizedQuantity *= 1000; }
        else if ("L".equals(normalizedUnit)) { normalizedUnit = "ml"; normalizedQuantity *= 1000; }
        ingredient.setUnit(normalizedUnit);
        ingredient.setQuantityPerServing(normalizedQuantity);
        ingredient.setName(ingredient.getName().trim());
        return ingredient;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) throw new RecipeException(RecipeErrorCode.INVALID_INGREDIENT_NAME);
        String normalizedName = name.trim();
        for (String forbidden : FORBIDDEN_EXPRESSIONS) {
            if (normalizedName.contains(forbidden))
                throw new RecipeException(RecipeErrorCode.FORBIDDEN_INGREDIENT_NAME, "금지 표현이 포함된 재료명입니다: " + normalizedName);
        }
    }

    private void validateQuantity(Double quantity) {
        if (quantity == null || quantity <= 0) throw new RecipeException(RecipeErrorCode.INVALID_INGREDIENT_QUANTITY);
    }

    private void validateUnit(String unit) {
        if (!StringUtils.hasText(unit)) throw new RecipeException(RecipeErrorCode.INVALID_INGREDIENT_UNIT);
        String normalizedUnit = unit.trim();
        if (!ALLOWED_UNITS.contains(normalizedUnit))
            throw new RecipeException(RecipeErrorCode.DISALLOWED_UNIT, "허용되지 않은 재료 단위입니다: " + normalizedUnit);
    }
}