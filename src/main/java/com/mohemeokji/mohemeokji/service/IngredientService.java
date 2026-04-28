package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.GroupedIngredientResponse;
import com.mohemeokji.mohemeokji.dto.IngredientSectionResponse;
import com.mohemeokji.mohemeokji.dto.IngredientRequest;
import com.mohemeokji.mohemeokji.dto.IngredientResponse;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import com.mohemeokji.mohemeokji.repository.IngredientRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
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
    public Long save(Long userId, IngredientRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다. id=" + userId));

        LocalDate purchaseDate = request.getPurchaseDate() != null ? request.getPurchaseDate() : LocalDate.now();
        LocalDate expiryDate = request.getExpiryDate() != null
                ? request.getExpiryDate()
                : purchaseDate.plusDays(ingredientShelfLifeService.resolveShelfLifeDays(request.getName()));

        if (expiryDate.isBefore(purchaseDate)) {
            throw new InvalidInputException("유통기한은 구매일보다 이전일 수 없습니다.");
        }

        Ingredient ingredient = Ingredient.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .purchaseDate(purchaseDate)
                .expiryDate(expiryDate)
                .category(request.getCategory())
                .user(user)
                .build();

        return ingredientRepository.save(ingredient).getId();
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> findMyIngredients(Long userId) {
        validateExistingUser(userId);
        return ingredientRepository.findByUserIdOrderByExpiryDateAsc(userId).stream()
                .map(ingredient -> IngredientResponse.from(
                        ingredient,
                        ingredientShelfLifeService.getExpiryPolicy(ingredient.getName())
                ))
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
            if (!item.getExpiryPolicy().isTrackExpiry()) {
                optionalItems.add(item);
                continue;
            }

            Integer alertThresholdDays = item.getExpiryPolicy().getAlertThresholdDays();
            boolean isImminent = item.getDday() != null
                    && alertThresholdDays != null
                    && item.getDday() <= alertThresholdDays;

            if (isImminent) {
                imminentItems.add(item);
                continue;
            }

            switch (item.getExpiryPolicy().getDisplayMode()) {
                case "LONG_TERM" -> longTermItems.add(item);
                case "OPTIONAL" -> optionalItems.add(item);
                default -> freshItems.add(item);
            }
        }

        List<IngredientSectionResponse> sections = new ArrayList<>();
        sections.add(new IngredientSectionResponse("imminent", "마감 임박", true, true, 1, imminentItems));
        sections.add(new IngredientSectionResponse("fresh", "신선 식재료", true, true, 2, freshItems));
        sections.add(new IngredientSectionResponse("long_term", "장기보관 / 조미료", true, true, 3, longTermItems));
        sections.add(new IngredientSectionResponse("optional", "제외 품목", true, true, 4, optionalItems));

        return new GroupedIngredientResponse(sections);
    }

    private void validateExistingUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("해당 유저가 없습니다. id=" + userId);
        }
    }

    @Transactional
    public void delete(Long ingredientId) {
        if (!ingredientRepository.existsById(ingredientId)) {
            throw new EntityNotFoundException("재료 없음 id=" + ingredientId);
        }
        ingredientRepository.deleteById(ingredientId);
    }

    @Transactional
    public void updateQuantity(Long id, Double quantity) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("재료 없음 id=" + id));
        ingredient.updateQuantity(quantity);
    }

    @Transactional
    public int deleteExpiredIngredients(Long userId) {
        validateExistingUser(userId);
        List<Ingredient> expiredIngredients = ingredientRepository.findByUserIdAndExpiryDateBefore(userId, LocalDate.now());
        int deletedCount = expiredIngredients.size();
        ingredientRepository.deleteAll(expiredIngredients);
        return deletedCount;
    }
}