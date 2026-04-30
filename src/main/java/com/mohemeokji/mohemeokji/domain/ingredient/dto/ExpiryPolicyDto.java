package com.mohemeokji.mohemeokji.domain.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpiryPolicyDto {
    private boolean trackExpiry;
    private Integer alertThresholdDays;
    private String displayMode;
    private String storageType;
}