package com.mohemeokji.mohemeokji.domain.recipe;

import com.mohemeokji.mohemeokji.domain.user.User;
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
@Table(name = "saved_recipes", uniqueConstraints = @UniqueConstraint(
        name = "uk_saved_recipes_user_normalized_name", columnNames = {"user_id", "normalized_recipe_name"}))
public class SavedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String recipeName;

    @Column(name = "normalized_recipe_name", nullable = false, length = 255)
    private String normalizedRecipeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String ytSearchQuery;
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
    public SavedRecipe(User user, String recipeName, String normalizedRecipeName, String description,
                       String ytSearchQuery, String youtubeUrl, String category, List<String> steps) {
        this.user = user;
        this.recipeName = recipeName;
        this.normalizedRecipeName = normalizedRecipeName;
        this.description = description;
        this.ytSearchQuery = ytSearchQuery;
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