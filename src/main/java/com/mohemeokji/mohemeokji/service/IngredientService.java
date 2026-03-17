package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(Long userId, String name, Double quantity, String unit, LocalDate expiryDate) {
        // 1. 재료를 넣을 유저를 먼저 찾고
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // 2. 재료 엔티티를 생성 (여기서 드디어 생성자가 사용됩니다! ㅋ)
        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .quantity(quantity)
                .unit(unit)
                .expiryDate(expiryDate)
                .user(user)
                .build();

        return ingredientRepository.save(ingredient).getId();
    }

    @Transactional(readOnly = true)
    public List<Ingredient> findMyIngredients(Long userId) {
        // 1. 유저가 있는지 검증하고 (성공하면 ID 반환, 실패하면 예외)
        Long validatedUserId = validateExistingUser(userId);

        // 2. 해당 유저의 재료 목록을 유통기한 순으로 반환
        return ingredientRepository.findByUserIdOrderByExpiryDateAsc(validatedUserId);
    }

    private Long validateExistingUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return userId;
    }
}