package com.mohemeokji.mohemeokji.dto;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class IngredientResponse {
    private Long id;
    private String name;
    private Double quantity;
    private String unit;
    private String category;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private Long dday; // 유통기한까지 남은 일수
    private ExpiryPolicyDto expiryPolicy;
    private LocalDateTime createdAt;

    public static IngredientResponse from(Ingredient ingredient, ExpiryPolicyDto expiryPolicy) {
        Long dday = null;
        if (ingredient.getExpiryDate() != null) {
            dday = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), ingredient.getExpiryDate());
        }

        return IngredientResponse.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .unit(ingredient.getUnit())
                .category(ingredient.getCategory())
                .purchaseDate(ingredient.getPurchaseDate())
                .expiryDate(ingredient.getExpiryDate())
                .dday(dday)
                .expiryPolicy(expiryPolicy)
                .createdAt(ingredient.getCreatedAt())
                .build();
    }
}
