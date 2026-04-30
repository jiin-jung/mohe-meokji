package com.mohemeokji.mohemeokji.domain.ingredient;

import com.mohemeokji.mohemeokji.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double quantity;
    private String unit;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private String category;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Ingredient(String name, Double quantity, String unit, LocalDate purchaseDate, LocalDate expiryDate, String category, User user) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.category = category;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void updateQuantity(double newQuantity) {
        this.quantity = newQuantity;
    }
}