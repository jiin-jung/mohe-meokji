package com.mohemeokji.mohemeokji.domain.disliked.service;

import com.mohemeokji.mohemeokji.domain.disliked.DislikedRecipe;
import com.mohemeokji.mohemeokji.domain.disliked.dto.DislikedRecipeRequest;
import com.mohemeokji.mohemeokji.domain.disliked.dto.DislikedRecipeResponse;
import com.mohemeokji.mohemeokji.domain.disliked.exception.DislikedRecipeErrorCode;
import com.mohemeokji.mohemeokji.domain.disliked.exception.DislikedRecipeException;
import com.mohemeokji.mohemeokji.domain.disliked.repository.DislikedRecipeRepository;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DislikedRecipeService {

    private final DislikedRecipeRepository dislikedRecipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addDislikedRecipe(Long userId, DislikedRecipeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        String normalizedRecipeName = normalizeRecipeName(request.getRecipeName());
        if (!StringUtils.hasText(normalizedRecipeName)) {
            throw new DislikedRecipeException(DislikedRecipeErrorCode.INVALID_RECIPE_NAME);
        }
        if (dislikedRecipeRepository.existsByUserIdAndNormalizedRecipeName(userId, normalizedRecipeName)) {
            throw new DislikedRecipeException(DislikedRecipeErrorCode.DUPLICATE_DISLIKE);
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
            throw new DislikedRecipeException(DislikedRecipeErrorCode.DISLIKED_RECIPE_NOT_FOUND,
                    "싫어요 레시피가 없습니다. id=" + dislikeId);
        }
        dislikedRecipeRepository.deleteById(dislikeId);
    }

    public Set<String> getDislikedRecipeNames(Long userId) {
        return dislikedRecipeRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(DislikedRecipe::getNormalizedRecipeName)
                .collect(Collectors.toSet());
    }

    private String normalizeRecipeName(String recipeName) {
        if (!StringUtils.hasText(recipeName)) {
            return null;
        }
        return recipeName.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }
}