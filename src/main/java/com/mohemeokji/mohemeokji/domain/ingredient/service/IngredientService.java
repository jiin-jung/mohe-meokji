package com.mohemeokji.mohemeokji.domain.ingredient.service;

import com.mohemeokji.mohemeokji.domain.ingredient.Ingredient;
import com.mohemeokji.mohemeokji.domain.ingredient.dto.*;
import com.mohemeokji.mohemeokji.domain.ingredient.exception.IngredientErrorCode;
import com.mohemeokji.mohemeokji.domain.ingredient.exception.IngredientException;
import com.mohemeokji.mohemeokji.domain.ingredient.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final IngredientShelfLifeService ingredientShelfLifeService;

    @Transactional
    public List<Long> saveAll(Long userId, List<IngredientRequest> requests) {
        return requests.stream().map(r -> save(userId, r)).toList();
    }

    @Transactional
    public Long save(Long userId, IngredientRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));
        LocalDate purchaseDate = request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDate.now();
        LocalDate expiryDate = request.getExpiryDate() != null
                ? request.getExpiryDate()
                : purchaseDate.plusDays(ingredientShelfLifeService.resolveShelfLifeDays(request.getName()));
        if (expiryDate.isBefore(purchaseDate)) {
            throw new IngredientException(IngredientErrorCode.INVALID_EXPIRY_DATE);
        }
        Ingredient ingredient = Ingredient.builder()
                .name(request.getName()).quantity(request.getQuantity()).unit(request.getUnit())
                .purchaseDate(purchaseDate).expiryDate(expiryDate).category(request.getCategory()).user(user).build();
        return ingredientRepository.save(ingredient).getId();
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> findMyIngredients(Long userId) {
        validateExistingUser(userId);
        return ingredientRepository.findByUserIdOrderByExpiryDateAsc(userId).stream()
                .map(i -> IngredientResponse.from(i, ingredientShelfLifeService.getExpiryPolicy(i.getName())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupedIngredientResponse findGroupedIngredients(Long userId) {
        List<IngredientResponse> items = findMyIngredients(userId);
        List<IngredientResponse> imminentItems = new ArrayList<>();
        List<IngredientResponse> freshItems = new ArrayList<>();
        List<IngredientResponse> longTermItems = new ArrayList<>();
        List<IngredientResponse> optionalItems = new ArrayList<>();
        for (IngredientResponse item : items) {
            if (!item.getExpiryPolicy().isTrackExpiry()) { optionalItems.add(item); continue; }
            Integer alertDays = item.getExpiryPolicy().getAlertThresholdDays();
            boolean isImminent = item.getDday() != null && alertDays != null && item.getDday() <= alertDays;
            if (isImminent) { imminentItems.add(item); continue; }
            switch (item.getExpiryPolicy().getDisplayMode()) {
                case "LONG_TERM" -> longTermItems.add(item);
                case "OPTIONAL" -> optionalItems.add(item);
                default -> freshItems.add(item);
            }
        }
        return new GroupedIngredientResponse(List.of(
                new IngredientSectionResponse("imminent", "마감 임박", true, true, 1, imminentItems),
                new IngredientSectionResponse("fresh", "신선 식재료", true, true, 2, freshItems),
                new IngredientSectionResponse("long_term", "장기보관 / 조미료", true, true, 3, longTermItems),
                new IngredientSectionResponse("optional", "제외 품목", true, true, 4, optionalItems)
        ));
    }

    @Transactional
    public void delete(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new IngredientException(IngredientErrorCode.INGREDIENT_NOT_FOUND, "재료가 없습니다. id=" + ingredientId);
        }
        ingredientRepository.deleteById(ingredientId);
    }

    @Transactional
    public void updateQuantity(Long id, Double quantity) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientException(IngredientErrorCode.INGREDIENT_NOT_FOUND, "재료가 없습니다. id=" + id));
        ingredient.updateQuantity(quantity);
    }

    @Transactional
    public int deleteExpiredIngredients(Long userId) {
        validateExistingUser(userId);
        List<Ingredient> expired = ingredientRepository.findByUserIdAndExpiryDateBefore(userId, LocalDate.now());
        ingredientRepository.deleteAll(expired);
        return expired.size();
    }

    private void validateExistingUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId);
        }
    }
}