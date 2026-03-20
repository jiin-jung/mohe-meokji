package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.IngredientRequest;
import com.mohemeokji.mohemeokji.dto.IngredientResponse;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userId, IngredientRequest request) {
        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        // 2. 재료 생성 (Builder 사용)
        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .expiryDate(request.getExpiryDate())
                .category(request.getCategory())
                .user(user)
                .build();

        return ingredientRepository.save(ingredient).getId();
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> findMyIngredients(Long userId) {
        // 유저 존재 여부 확인
        validateExistingUser(userId);

        // 유통기한 순으로 재료 목록 반환
        return ingredientRepository.findByUserIdOrderByExpiryDateAsc(userId).stream()
                .map(IngredientResponse::from)
                .collect(Collectors.toList());
    }

    private void validateExistingUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("해당 유저가 없습니다. id=" + userId);
        }
    }

    @Transactional
    public void delete(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }

    @Transactional
    public void updateQuantity(Long id, Double quantity) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재료 없음 id=" + id));
        // 더티 체킹을 통해 수량 변경
        ingredient.updateQuantity(quantity);
    }
}