package com.mohemeokji.mohemeokji.domain.scan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScanIngredientDto {
    private String name;
    private String icon;
    private String category;
}