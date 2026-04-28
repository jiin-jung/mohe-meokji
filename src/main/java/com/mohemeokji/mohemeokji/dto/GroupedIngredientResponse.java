package com.mohemeokji.mohemeokji.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupedIngredientResponse {
    private List<IngredientSectionResponse> sections;
}
