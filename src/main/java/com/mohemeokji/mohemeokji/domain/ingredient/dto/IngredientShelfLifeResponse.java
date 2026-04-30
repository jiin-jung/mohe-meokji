package com.mohemeokji.mohemeokji.domain.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IngredientShelfLifeResponse {
    private String inputName;
    private String matchedName;
    private Integer defaultShelfLifeDays;
    private ExpiryPolicyDto expiryPolicy;
}