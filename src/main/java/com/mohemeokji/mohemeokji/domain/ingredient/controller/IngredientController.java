package com.mohemeokji.mohemeokji.domain.ingredient.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.domain.ingredient.dto.*;
import com.mohemeokji.mohemeokji.domain.ingredient.service.IngredientService;
import com.mohemeokji.mohemeokji.domain.ingredient.service.IngredientShelfLifeService;
import com.mohemeokji.mohemeokji.global.dto.ApiMessageResponse;
import com.mohemeokji.mohemeokji.global.dto.IdResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:5173")
public class IngredientController {

    private final IngredientService ingredientService;
    private final IngredientShelfLifeService ingredientShelfLifeService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/me")
    @Operation(summary = "냉장고 재료 추가")
    public IdResponse save(@Valid @RequestBody IngredientRequest request) {
        return new IdResponse(ingredientService.save(currentUserProvider.getCurrentUserId(), request));
    }

    @PostMapping("/me/batch")
    @Operation(summary = "냉장고 재료 일괄 추가")
    public Map<String, Object> saveBatch(@Valid @RequestBody List<IngredientRequest> requests) {
        List<Long> ids = ingredientService.saveAll(currentUserProvider.getCurrentUserId(), requests);
        return Map.of("ids", ids, "count", ids.size());
    }

    @GetMapping("/me")
    @Operation(summary = "내 냉장고 조회")
    public List<IngredientResponse> getMyFridge() {
        return ingredientService.findMyIngredients(currentUserProvider.getCurrentUserId());
    }

    @GetMapping("/me/grouped")
    @Operation(summary = "내 냉장고 그룹 조회")
    public GroupedIngredientResponse getGroupedFridge() {
        return ingredientService.findGroupedIngredients(currentUserProvider.getCurrentUserId());
    }

    @GetMapping("/shelf-life")
    @Operation(summary = "재료 기본 보관일 조회")
    public IngredientShelfLifeResponse getShelfLife(@RequestParam String name) {
        return ingredientShelfLifeService.getShelfLife(name);
    }

    @DeleteMapping("/{ingredientId}")
    @Operation(summary = "재료 삭제")
    public ApiMessageResponse delete(@PathVariable Long ingredientId) {
        ingredientService.delete(ingredientId);
        return new ApiMessageResponse("재료가 삭제되었습니다.");
    }

    @PatchMapping("/{id}/quantity")
    public ApiMessageResponse updateQuantity(@PathVariable Long id, @RequestParam @Positive Double quantity) {
        ingredientService.updateQuantity(id, quantity);
        return new ApiMessageResponse("재료 수량이 수정되었습니다.");
    }

    @DeleteMapping("/expired/me")
    @Operation(summary = "만료 재료 일괄 삭제")
    public ApiMessageResponse deleteExpiredIngredients() {
        int count = ingredientService.deleteExpiredIngredients(currentUserProvider.getCurrentUserId());
        return new ApiMessageResponse(count + "개의 만료 재료가 삭제되었습니다.");
    }
}