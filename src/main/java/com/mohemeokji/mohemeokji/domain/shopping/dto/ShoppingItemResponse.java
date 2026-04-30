package com.mohemeokji.mohemeokji.domain.shopping.dto;

import com.mohemeokji.mohemeokji.domain.shopping.ShoppingItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ShoppingItemResponse {
    private Long id;
    private String name;
    private Double quantity;
    private String unit;
    private String category;
    private String sourceRecipeName;
    private LocalDateTime createdAt;

    public static ShoppingItemResponse from(ShoppingItem shoppingItem) {
        return new ShoppingItemResponse(
                shoppingItem.getId(),
                shoppingItem.getName(),
                shoppingItem.getQuantity(),
                shoppingItem.getUnit(),
                shoppingItem.getCategory(),
                shoppingItem.getSourceRecipeName(),
                shoppingItem.getCreatedAt()
        );
    }
}