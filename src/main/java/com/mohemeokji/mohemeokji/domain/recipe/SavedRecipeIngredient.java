package com.mohemeokji.mohemeokji.domain.recipe;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "saved_recipe_ingredients")
public class SavedRecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private SavedRecipe savedRecipe;

    @Column(nullable = false)
    private String name;

    private Double quantityPerServing;
    private String unit;
    private String category;

    @Builder
    public SavedRecipeIngredient(String name, Double quantityPerServing, String unit, String category) {
        this.name = name;
        this.quantityPerServing = quantityPerServing;
        this.unit = unit;
        this.category = category;
    }
}