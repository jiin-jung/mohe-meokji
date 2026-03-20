package com.mohemeokji.mohemeokji.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {
    private String name;
    private Double quantity;
    private String unit;
    private LocalDate expiryDate;
    private String category;
}