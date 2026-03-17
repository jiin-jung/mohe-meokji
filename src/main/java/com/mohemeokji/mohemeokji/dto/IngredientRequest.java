package com.mohemeokji.mohemeokji.dto;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class IngredientRequest {
    private String name;           // 재료명
    private Double quantity;       // 수량
    private String unit;           // 단위
    private LocalDate expiryDate;  // 유통기한
}