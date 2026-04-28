package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.dto.ApiMessageResponse;
import com.mohemeokji.mohemeokji.dto.GroupedIngredientResponse;
import com.mohemeokji.mohemeokji.dto.IdResponse;
import com.mohemeokji.mohemeokji.dto.IngredientRequest;
import com.mohemeokji.mohemeokji.dto.IngredientResponse;
import com.mohemeokji.mohemeokji.dto.IngredientShelfLifeResponse;
import com.mohemeokji.mohemeokji.service.IngredientService;
import com.mohemeokji.mohemeokji.service.IngredientShelfLifeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:5173") // 프론트엔드 포트 허용
public class IngredientController {

    private final IngredientService ingredientService;
    private final IngredientShelfLifeService ingredientShelfLifeService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/me")
    @Operation(summary = "냉장고 재료 추가", description = "특정 유저의 냉장고에 식재료를 등록합니다.")
    public IdResponse save(@Valid @RequestBody IngredientRequest request) {
        Long ingredientId = ingredientService.save(currentUserProvider.getCurrentUserId(), request);
        return new IdResponse(ingredientId);
    }

    @GetMapping("/me")
    @Operation(summary = "내 냉장고 조회", description = "유통기한 임박 순으로 재료 목록을 조회합니다.")
    public List<IngredientResponse> getMyFridge() {
        return ingredientService.findMyIngredients(currentUserProvider.getCurrentUserId());
    }

    @GetMapping("/me/grouped")
    @Operation(summary = "내 냉장고 그룹 조회", description = "UI 섹션 구분과 기본 펼침 상태를 포함한 재료 목록을 조회합니다.")
    public GroupedIngredientResponse getGroupedFridge() {
        return ingredientService.findGroupedIngredients(currentUserProvider.getCurrentUserId());
    }

    @GetMapping("/shelf-life")
    @Operation(summary = "재료 기본 보관일 조회", description = "재료명 기준 기본 보관일과 보관 방식을 조회합니다.")
    public IngredientShelfLifeResponse getShelfLife(@RequestParam String name) {
        return ingredientShelfLifeService.getShelfLife(name);
    }

    @DeleteMapping("/{ingredientId}")
    @Operation(summary = "재료 삭제", description = "냉장고에서 특정 재료를 제거합니다.")
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
    @Operation(summary = "만료 재료 일괄 삭제", description = "유통기한이 지난 재료를 한 번에 삭제합니다.")
    public ApiMessageResponse deleteExpiredIngredients() {
        int deletedCount = ingredientService.deleteExpiredIngredients(currentUserProvider.getCurrentUserId());
        return new ApiMessageResponse(deletedCount + "개의 만료 재료가 삭제되었습니다.");
    }

}
