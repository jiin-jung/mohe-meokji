package com.mohemeokji.mohemeokji.domain.disliked;

import com.mohemeokji.mohemeokji.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "disliked_recipes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_disliked_recipes_user_normalized_name",
                columnNames = {"user_id", "normalized_recipe_name"}
        )
)
public class DislikedRecipe {

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

    private String category;

    private LocalDateTime createdAt;

    @Builder
    public DislikedRecipe(User user, String recipeName, String normalizedRecipeName, String category) {
        this.user = user;
        this.recipeName = recipeName;
        this.normalizedRecipeName = normalizedRecipeName;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }
}