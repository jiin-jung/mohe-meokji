package com.mohemeokji.mohemeokji.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class IngredientSectionResponse {
    private String key;
    private String title;
    private boolean collapsible;
    private boolean defaultExpanded;
    private int sortOrder;
    private List<IngredientResponse> items;
}
