package com.mohemeokji.mohemeokji.domain;

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

    private String name;      // 재료명
    private Double quantity;  // 수량
    private String unit;      // 단위
    private LocalDate purchaseDate; // 구매일 또는 보관 시작일
    private LocalDate expiryDate; // 유통기한
    private String category; // 육류, 채소, 과일, 유제품 등

    private LocalDateTime createdAt;

    // 이 재료가 어느 유저의 냉장고에 있는지 연결
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
        this.category = category; // 'this.' 붙어있는지 확인!
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public void updateQuantity(double newQuantity) {
        this.quantity = newQuantity;
    }
}
