package com.mohemeokji.mohemeokji.repository;

import com.mohemeokji.mohemeokji.domain.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
    List<ShoppingItem> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<ShoppingItem> findByUserIdAndNameAndUnit(Long userId, String name, String unit);
}
