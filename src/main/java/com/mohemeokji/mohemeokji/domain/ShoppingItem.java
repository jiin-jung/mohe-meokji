package com.mohemeokji.mohemeokji.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "shopping_items")
public class ShoppingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    private Double quantity;

    private String unit;

    private String category;

    private String sourceRecipeName;

    private LocalDateTime createdAt;

    @Builder
    public ShoppingItem(User user, String name, Double quantity, String unit, String category, String sourceRecipeName) {
        this.user = user;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.sourceRecipeName = sourceRecipeName;
        this.createdAt = LocalDateTime.now();
    }

    public void increaseQuantity(Double amount) {
        if (amount == null) {
            return;
        }
        this.quantity = (this.quantity == null ? 0.0 : this.quantity) + amount;
    }
}
