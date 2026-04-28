package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.ShoppingItem;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.dto.ShoppingItemResponse;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import com.mohemeokji.mohemeokji.repository.ShoppingItemRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingListService {

    private final ShoppingItemRepository shoppingItemRepository;
    private final UserRepository userRepository;
    private final RecipeIngredientPolicyService recipeIngredientPolicyService;

    public List<ShoppingItemResponse> getShoppingItems(Long userId) {
        return shoppingItemRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ShoppingItemResponse::from)
                .toList();
    }

    @Transactional
    public int addMissingIngredients(Long userId, RecipeRecommendationDto recipe) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<RecipeRecommendationDto.NeededIngredientDto> missingIngredients = recipe.getMissingIngredients();
        if (missingIngredients == null || missingIngredients.isEmpty()) {
            throw new InvalidInputException("장보기 목록에 추가할 결핍 재료가 없습니다.");
        }
        recipeIngredientPolicyService.validateMissingIngredients(missingIngredients);

        for (RecipeRecommendationDto.NeededIngredientDto missingIngredient : missingIngredients) {
            ShoppingItem shoppingItem = shoppingItemRepository
                    .findByUserIdAndNameAndUnit(userId, missingIngredient.getName(), missingIngredient.getUnit())
                    .orElseGet(() -> ShoppingItem.builder()
                            .user(user)
                            .name(missingIngredient.getName())
                            .quantity(0.0)
                            .unit(missingIngredient.getUnit())
                            .category(missingIngredient.getCategory())
                            .sourceRecipeName(recipe.getRecipeName())
                            .build());

            shoppingItem.increaseQuantity(missingIngredient.getQuantity());
            shoppingItemRepository.save(shoppingItem);
        }

        return missingIngredients.size();
    }

    @Transactional
    public void deleteShoppingItem(Long shoppingItemId) {
        if (!shoppingItemRepository.existsById(shoppingItemId)) {
            throw new EntityNotFoundException("장보기 항목이 없습니다. id=" + shoppingItemId);
        }
        shoppingItemRepository.deleteById(shoppingItemId);
    }
}