package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.DislikedRecipe;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.DislikedRecipeRequest;
import com.mohemeokji.mohemeokji.dto.DislikedRecipeResponse;
import com.mohemeokji.mohemeokji.exception.DuplicateResourceException;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
import com.mohemeokji.mohemeokji.repository.DislikedRecipeRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DislikedRecipeService {

    private final DislikedRecipeRepository dislikedRecipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addDislikedRecipe(Long userId, DislikedRecipeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        String normalizedRecipeName = normalizeRecipeName(request.getRecipeName());
        if (!StringUtils.hasText(normalizedRecipeName)) {
            throw new InvalidInputException("레시피 이름이 올바르지 않습니다.");
        }
        if (dislikedRecipeRepository.existsByUserIdAndNormalizedRecipeName(userId, normalizedRecipeName)) {
            throw new DuplicateResourceException("이미 싫어요 처리한 레시피입니다.");
        }

        DislikedRecipe dislikedRecipe = DislikedRecipe.builder()
                .user(user)
                .recipeName(request.getRecipeName())
                .normalizedRecipeName(normalizedRecipeName)
                .category(request.getCategory())
                .build();

        return dislikedRecipeRepository.save(dislikedRecipe).getId();
    }

    public List<DislikedRecipeResponse> getDislikedRecipes(Long userId) {
        return dislikedRecipeRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(DislikedRecipeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteDislikedRecipe(Long dislikeId) {
        if (!dislikedRecipeRepository.existsById(dislikeId)) {
            throw new EntityNotFoundException("싫어요 레시피가 없습니다. id=" + dislikeId);
        }
        dislikedRecipeRepository.deleteById(dislikeId);
    }

    public Set<String> getDislikedRecipeNames(Long userId) {
        return dislikedRecipeRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(DislikedRecipe::getNormalizedRecipeName)
                .collect(java.util.stream.Collectors.toSet());
    }

    private String normalizeRecipeName(String recipeName) {
        if (!StringUtils.hasText(recipeName)) {
            return null;
        }
        return recipeName.replaceAll("\\s+", "")
                .toLowerCase(Locale.ROOT);
    }
}