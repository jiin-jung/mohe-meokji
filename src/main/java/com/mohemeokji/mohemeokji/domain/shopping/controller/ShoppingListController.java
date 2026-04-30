package com.mohemeokji.mohemeokji.domain.shopping.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.shopping.dto.ShoppingItemResponse;
import com.mohemeokji.mohemeokji.domain.shopping.service.ShoppingListService;
import com.mohemeokji.mohemeokji.global.dto.ApiMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shopping-list")
@CrossOrigin(origins = "http://localhost:5173")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/me")
    public List<ShoppingItemResponse> getMyShoppingList() {
        return shoppingListService.getShoppingItems(currentUserProvider.getCurrentUserId());
    }

    @PostMapping("/from-recipe/me")
    public ApiMessageResponse addMissingIngredients(@Valid @RequestBody RecipeRecommendationDto recipe) {
        int addedCount = shoppingListService.addMissingIngredients(currentUserProvider.getCurrentUserId(), recipe);
        return new ApiMessageResponse(addedCount + "개의 결핍 재료가 장보기 목록에 추가되었습니다.");
    }

    @DeleteMapping("/{shoppingItemId}")
    public ApiMessageResponse deleteShoppingItem(@PathVariable Long shoppingItemId) {
        shoppingListService.deleteShoppingItem(shoppingItemId);
        return new ApiMessageResponse("장보기 항목이 삭제되었습니다.");
    }

    @DeleteMapping("/me")
    public ApiMessageResponse deleteAllShoppingItems() {
        shoppingListService.deleteAll(currentUserProvider.getCurrentUserId());
        return new ApiMessageResponse("장보기 목록이 초기화되었습니다.");
    }
}