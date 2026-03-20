package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.IngredientRequest;
import com.mohemeokji.mohemeokji.dto.IngredientResponse;
import com.mohemeokji.mohemeokji.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:5173") // 프론트엔드 포트 허용
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping("/{userId}")
    @Operation(summary = "냉장고 재료 추가", description = "특정 유저의 냉장고에 식재료를 등록합니다.")
    public String save(@PathVariable Long userId, @RequestBody IngredientRequest request) {
        // 서비스의 save(Long userId, IngredientRequest request) 메서드에 맞춰 객체 전달
        Long ingredientId = ingredientService.save(userId, request);
        return "식재료 등록 완료! ID: " + ingredientId;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "내 냉장고 조회", description = "유통기한 임박 순으로 재료 목록을 조회합니다.")
    public List<IngredientResponse> getMyFridge(@PathVariable Long userId) {
        return ingredientService.findMyIngredients(userId);
    }

    @DeleteMapping("/{ingredientId}")
    @Operation(summary = "재료 삭제", description = "냉장고에서 특정 재료를 제거합니다.")
    public void delete(@PathVariable Long ingredientId) {
        ingredientService.delete(ingredientId);
    }

    @PatchMapping("/{id}/quantity")
    public void updateQuantity(@PathVariable Long id, @RequestParam Double quantity) {
        ingredientService.updateQuantity(id, quantity);
    }

}