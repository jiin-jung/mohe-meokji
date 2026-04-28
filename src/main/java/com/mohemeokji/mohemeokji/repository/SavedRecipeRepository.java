package com.mohemeokji.mohemeokji.repository;

import com.mohemeokji.mohemeokji.domain.SavedRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {
    List<SavedRecipe> findByUserId(Long userId);
    Optional<SavedRecipe> findByUserIdAndNormalizedRecipeName(Long userId, String normalizedRecipeName);
    boolean existsByUserIdAndNormalizedRecipeName(Long userId, String normalizedRecipeName);
}
