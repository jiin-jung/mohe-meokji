package com.mohemeokji.mohemeokji.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;      // 재료명
    private Double quantity;  // 수량
    private String unit;      // 단위
    private LocalDate expiryDate; // 유통기한

    // 이 재료가 어느 유저의 냉장고에 있는지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Ingredient(String name, Double quantity, String unit, LocalDate expiryDate, User user) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.user = user;
    }
}