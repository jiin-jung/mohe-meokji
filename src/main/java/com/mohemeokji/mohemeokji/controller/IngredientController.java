package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.IngredientRequest;
import com.mohemeokji.mohemeokji.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    @PostMapping("/{userId}")
    @Operation(summary = "냉장고 재료 추가", description = "특정 유저의 냉장고에 새로운 식재료를 넣습니다.")
    public String save(@PathVariable Long userId, @RequestBody IngredientRequest request) {
        Long ingredientId = ingredientService.save(
                userId,
                request.getName(),
                request.getQuantity(),
                request.getUnit(),
                request.getExpiryDate()
        );
        return "식재료 등록 완료! ID: " + ingredientId;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "내 냉장고 조회", description = "유통기한이 임박한 순서대로 재료 목록을 가져옵니다.")
    public List<Ingredient> getMyFridge(@PathVariable Long userId) {
        return ingredientService.findMyIngredients(userId);
    }
}