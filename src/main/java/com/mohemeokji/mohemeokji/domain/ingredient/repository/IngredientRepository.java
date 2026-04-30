package com.mohemeokji.mohemeokji.domain.ingredient.repository;

import com.mohemeokji.mohemeokji.domain.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByUserIdOrderByExpiryDateAsc(Long userId);
    List<Ingredient> findByUserId(Long userId);
    List<Ingredient> findByUserIdAndExpiryDateBefore(Long userId, LocalDate date);
}