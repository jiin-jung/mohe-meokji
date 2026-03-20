package com.mohemeokji.mohemeokji.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "saved_recipes")
public class SavedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String recipeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String youtubeUrl;

    private String category;

    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step")
    @OrderColumn(name = "step_order")
    private List<String> steps = new ArrayList<>();

    @OneToMany(mappedBy = "savedRecipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedRecipeIngredient> ingredients = new ArrayList<>();

    private LocalDateTime createdAt;

    @Builder
    public SavedRecipe(User user, String recipeName, String description, String youtubeUrl, String category, List<String> steps) {
        this.user = user;
        this.recipeName = recipeName;
        this.description = description;
        this.youtubeUrl = youtubeUrl;
        this.category = category;
        this.steps = steps != null ? steps : new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public void addIngredient(SavedRecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setSavedRecipe(this);
    }
}
