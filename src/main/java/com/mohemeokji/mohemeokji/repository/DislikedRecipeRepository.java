package com.mohemeokji.mohemeokji.repository;

import com.mohemeokji.mohemeokji.domain.DislikedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface DislikedRecipeRepository extends JpaRepository<DislikedRecipe, Long> {
    List<DislikedRecipe> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndNormalizedRecipeName(Long userId, String normalizedRecipeName);
    Set<DislikedRecipe> findByUserIdAndNormalizedRecipeNameIn(Long userId, Set<String> normalizedRecipeNames);
}
