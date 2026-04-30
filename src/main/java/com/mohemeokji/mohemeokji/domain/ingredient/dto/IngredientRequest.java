package com.mohemeokji.mohemeokji.domain.ingredient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRequest {
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Double quantity;

    @NotBlank
    private String unit;

    private LocalDate purchaseDate;
    private LocalDate expiryDate;

    @NotBlank
    private String category;
}