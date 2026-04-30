package com.mohemeokji.mohemeokji.domain.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupedIngredientResponse {
    private List<IngredientSectionResponse> sections;
}