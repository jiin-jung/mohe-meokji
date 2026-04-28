package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CookService {

    private final IngredientRepository ingredientRepository;

    public int calculateMaxServings(Long userId, RecipeRecommendationDto recipe) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return 0;
        }

        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);
        double maxServings = 5.0;

        List<String> coreCategories = List.of("육류", "해산물");
        List<String> coreNames = List.of("달걀", "계란", "우유");

        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            boolean isCore = coreCategories.contains(required.getCategory()) ||
                    coreNames.stream().anyMatch(name -> required.getName().contains(name));

            if (!isCore) continue;

            Ingredient stock = myIngredients.stream()
                    .filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName()))
                    .findFirst()
                    .orElse(null);

            if (stock == null) return 0;

            if (required.getQuantityPerServing() == null || required.getQuantityPerServing() <= 0) {
                throw new InvalidInputException("레시피 재료 수량이 올바르지 않습니다: " + required.getName());
            }

            double possible = stock.getQuantity() / required.getQuantityPerServing();
            maxServings = Math.min(maxServings, possible);
        }

        return (int) Math.floor(maxServings);
    }

    @Transactional
    public void cook(Long userId, RecipeRecommendationDto recipe, int servings) {
        int maxServings = calculateMaxServings(userId, recipe);
        if (maxServings <= 0) {
            throw new InvalidInputException("현재 재고로는 해당 레시피를 조리할 수 없습니다.");
        }
        if (servings > maxServings) {
            throw new InvalidInputException("요청 인분이 재고보다 많습니다. 최대 가능 인분: " + maxServings);
        }

        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);

        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            Ingredient stock = myIngredients.stream()
                    .filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("재료를 찾을 수 없습니다: " + required.getName()));

            double totalRequired = required.getQuantityPerServing() * servings;
            double remaining = stock.getQuantity() - totalRequired;

            if (remaining <= 0) {
                ingredientRepository.delete(stock);
            } else {
                stock.updateQuantity(remaining);
            }
        }
    }
}