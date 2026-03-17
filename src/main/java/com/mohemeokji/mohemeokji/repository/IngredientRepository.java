package com.mohemeokji.mohemeokji.repository;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    // 유저 ID로 찾되, 유통기한(ExpiryDate) 기준 오름차순(Asc)으로 정렬!
    List<Ingredient> findByUserIdOrderByExpiryDateAsc(Long userId);
}