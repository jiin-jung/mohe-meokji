package com.mohemeokji.mohemeokji.dto;

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
