package com.mohemeokji.mohemeokji.domain.recipe.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohemeokji.mohemeokji.domain.ingredient.Ingredient;
import com.mohemeokji.mohemeokji.domain.recipe.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.domain.recipe.exception.GeminiErrorCode;
import com.mohemeokji.mohemeokji.domain.recipe.exception.GeminiException;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeErrorCode;
import com.mohemeokji.mohemeokji.domain.recipe.exception.RecipeException;
import com.mohemeokji.mohemeokji.domain.user.dto.UserSettingsDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    private final RecipeIngredientPolicyService recipeIngredientPolicyService;

    @Value("${spring.gemini.api.key}")
    private String apiKey;

    @Value("${spring.gemini.api.url}")
    private String apiUrl;

    public List<RecipeRecommendationDto> getRecipeRecommendations(List<Ingredient> ingredients, UserSettingsDto settings) {
        if (ingredients == null || ingredients.isEmpty()) throw new RecipeException(RecipeErrorCode.INSUFFICIENT_INGREDIENTS);

        String ingredientsInfo = ingredients.stream()
                .map(i -> i.getName() + "(" + i.getCategory() + "):" + i.getQuantity() + i.getUnit())
                .collect(Collectors.joining(","));
        String userConstraints = buildUserConstraints(settings);

        String prompt = String.format(
                "내 냉장고 재료: [%s]. %s" +
                "1. 이 재료들로 만들 수 있는 한국 요리 레시피 3~5개를 추천해줘. " +
                "2. JSON 구조는 다음과 같아야 해: " +
                "[{ \"recipeName\": \"요리명\", \"description\": \"설명\", \"ytSearchQuery\": \"요리명 + 레시피\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\", \"ingredients\": [{ \"name\": \"재료명\", \"quantityPerServing\": 0.0, \"unit\": \"단위\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\" }], \"maxServings\": 2, \"steps\": [\"1단계\", \"2단계\"] }] " +
                "3. ingredients의 각 항목은 반드시 name, quantityPerServing, unit, category를 포함해야 한다. " +
                "4. quantityPerServing는 반드시 숫자형이어야 하며 0보다 커야 한다. " +
                "5. unit은 허용 단위만 사용한다: g, kg, ml, L, 개, 알, 장, 줄기, 쪽, 모, 단, 큰술, 작은술, 컵. " +
                "6. 적당량, 조금, 약간, 한 줌, 한줌, 약간의, 취향껏 같은 표현은 절대 사용하지 않는다. " +
                "7. 응답은 반드시 마크다운 없이 순수 JSON 배열([]) 형식으로만 출력한다. " +
                "8. 'recipeName' 필드명을 반드시 지켜라.",
                ingredientsInfo, userConstraints);

        String responseJson = webClientBuilder.build().post()
                .uri(apiUrl + "?key=" + apiKey)
                .bodyValue(Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))))
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(err -> { log.error("Gemini API 오류: {}", err); return clientResponse.createException(); }))
                .bodyToMono(String.class).block();

        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidate = root.path("candidates");
            if (!candidate.isArray() || candidate.isEmpty()) throw new GeminiException(GeminiErrorCode.EMPTY_RESPONSE);
            String contentText = candidate.get(0).path("content").path("parts").get(0).path("text").asText();
            if (!StringUtils.hasText(contentText)) throw new GeminiException(GeminiErrorCode.EMPTY_RESPONSE_BODY);
            contentText = contentText.replaceAll("```json", "").replaceAll("```", "").trim();
            List<RecipeRecommendationDto> recipes = objectMapper.readValue(contentText, new TypeReference<>() {});
            recipeIngredientPolicyService.normalizeAndValidateRecommendations(recipes);
            return recipes;
        } catch (GeminiException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
            throw new GeminiException(GeminiErrorCode.PARSE_ERROR, "AI 레시피 파싱 중 오류 발생");
        }
    }

    private String buildUserConstraints(UserSettingsDto settings) {
        if (settings == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean hasConstraint = false;
        List<String> allergies = settings.getAllergies();
        List<String> disliked = settings.getDislikedIngredients();
        Integer servings = settings.getDefaultServings();
        if (allergies != null && !allergies.isEmpty()) { sb.append("- 알레르기 재료 포함 메뉴 제외: ").append(String.join(", ", allergies)).append("\n"); hasConstraint = true; }
        if (disliked != null && !disliked.isEmpty()) { sb.append("- 비선호 재료 포함 메뉴 제외 또는 대체 제안: ").append(String.join(", ", disliked)).append("\n"); hasConstraint = true; }
        if (servings != null && servings > 1) { sb.append("- 기본 인분 기준: ").append(servings).append("인분\n"); hasConstraint = true; }
        if (!hasConstraint) return "";
        return "다음 조건을 반드시 지켜주세요:\n" + sb;
    }
}