package com.mohemeokji.mohemeokji.domain.disliked.dto;

import com.mohemeokji.mohemeokji.domain.disliked.DislikedRecipe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DislikedRecipeResponse {
    private Long id;
    private String recipeName;
    private String category;
    private LocalDateTime createdAt;

    public static DislikedRecipeResponse from(DislikedRecipe dislikedRecipe) {
        return new DislikedRecipeResponse(
                dislikedRecipe.getId(),
                dislikedRecipe.getRecipeName(),
                dislikedRecipe.getCategory(),
                dislikedRecipe.getCreatedAt()
        );
    }
}