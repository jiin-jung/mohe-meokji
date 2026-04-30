package com.mohemeokji.mohemeokji.domain.shopping.service;

import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.recipe.service.RecipeIngredientPolicyService;
import com.mohemeokji.mohemeokji.domain.shopping.ShoppingItem;
import com.mohemeokji.mohemeokji.domain.shopping.dto.ShoppingItemResponse;
import com.mohemeokji.mohemeokji.domain.shopping.exception.ShoppingErrorCode;
import com.mohemeokji.mohemeokji.domain.shopping.exception.ShoppingException;
import com.mohemeokji.mohemeokji.domain.shopping.repository.ShoppingItemRepository;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
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
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        List<RecipeRecommendationDto.NeededIngredientDto> missingIngredients = recipe.getMissingIngredients();
        if (missingIngredients == null || missingIngredients.isEmpty()) {
            throw new ShoppingException(ShoppingErrorCode.EMPTY_SHORTAGE_LIST);
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
            throw new ShoppingException(ShoppingErrorCode.SHOPPING_ITEM_NOT_FOUND,
                    "장보기 항목이 없습니다. id=" + shoppingItemId);
        }
        shoppingItemRepository.deleteById(shoppingItemId);
    }

    @Transactional
    public void deleteAll(Long userId) {
        shoppingItemRepository.deleteAllByUserId(userId);
    }
}