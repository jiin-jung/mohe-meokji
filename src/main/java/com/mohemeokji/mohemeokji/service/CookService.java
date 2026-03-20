package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CookService {

    private final IngredientRepository ingredientRepository;

    // 레시피와 냉장고 재고를 비교하여 최대 조리 가능 인분수를 계산

    public int calculateMaxServings(Long userId, RecipeRecommendationDto recipe) {
        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);
        double maxServings = 5.0; // 기본 최대 제한 (필요에 따라 조절)

        // 핵심 비교 대상 리스트
        List<String> coreCategories = List.of("육류", "해산물");
        List<String> coreNames = List.of("달걀", "계란", "우유");

        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            // 1. 핵심 재료인지 확인 (카테고리 혹은 이름 기반)
            boolean isCore = coreCategories.contains(required.getCategory()) ||
                    coreNames.stream().anyMatch(name -> required.getName().contains(name));

            if (!isCore) continue; // 핵심 재료가 아니면 인분 계산에서 제외

            // 2. 재고 확인
            Ingredient stock = myIngredients.stream()
                    .filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName()))
                    .findFirst()
                    .orElse(null);

            if (stock == null) return 0; // 핵심 재료가 없으면 요리 불가

            double possible = stock.getQuantity() / required.getQuantityPerServing();
            maxServings = Math.min(maxServings, possible);
        }

        return (int) Math.floor(maxServings);
    }

    // 실제 들어간 인분만큼 재료 차감
    @Transactional // 작업 중 하나라도 실패하면 모두 취소
    public void cook(Long userId, RecipeRecommendationDto recipe, int servings) {
        List<Ingredient> myIngredients = ingredientRepository.findByUserId(userId);

        for (RecipeRecommendationDto.IngredientDto required : recipe.getIngredients()) {
            // 1. 이름으로 재료 찾기
            Ingredient stock = myIngredients.stream()
                    .filter(i -> i.getName().contains(required.getName()) || required.getName().contains(i.getName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("재료를 찾을 수 없습니다: " + required.getName()));

            // 2. 차감할 총량 계산
            double totalRequired = required.getQuantityPerServing() * servings;

            // 3. 재고 수정
            double remaining = stock.getQuantity() - totalRequired;

            if (remaining <= 0) {
                // 수량이 0 이하면 재료 삭제
                ingredientRepository.delete(stock);
            } else {
                // 수량 업데이트 (Dirty Checking에 의해 자동 반영)
                stock.updateQuantity(remaining);
            }
        }
    }
}