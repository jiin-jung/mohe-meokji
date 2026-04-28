package com.mohemeokji.mohemeokji.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import com.mohemeokji.mohemeokji.exception.ExternalServiceException;
import com.mohemeokji.mohemeokji.exception.InvalidInputException;
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

    public List<RecipeRecommendationDto> getRecipeRecommendations(List<Ingredient> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new InvalidInputException("추천을 위해 최소 1개 이상의 재료가 필요합니다.");
        }

        String ingredientsInfo = ingredients.stream()
                .map(i -> i.getName() + "(" + i.getCategory() + "):" + i.getQuantity() + i.getUnit())
                .collect(Collectors.joining(","));

        String prompt = String.format(
                "내 냉장고 재료: [%s]. " +
                        "1. 이 재료들로 만들 수 있는 한국 요리 레시피 3~5개를 추천해줘. " +
                        "2. JSON 구조는 다음과 같아야 해: " +
                        "[{ \"recipeName\": \"요리명\", \"description\": \"설명\", \"ytSearchQuery\": \"요리명 + 레시피\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\", \"ingredients\": [{ \"name\": \"재료명\", \"quantityPerServing\": 0.0, \"unit\": \"단위\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\" }], \"maxServings\": 2, \"steps\": [\"1단계\", \"2단계\"] }] " +
                        "3. ingredients의 각 항목은 반드시 name, quantityPerServing, unit, category를 포함해야 한다. " +
                        "4. quantityPerServing는 반드시 숫자형이어야 하며 0보다 커야 한다. " +
                        "5. unit은 허용 단위만 사용한다: g, kg, ml, L, 개, 알, 장, 줄기, 쪽, 모, 단, 큰술, 작은술, 컵. " +
                        "6. 고형 재료는 가능하면 g, 액체 재료는 가능하면 ml를 사용한다. " +
                        "7. 개수형 식재료에만 개, 알, 장, 줄기, 쪽, 모, 단을 사용한다. " +
                        "8. 적당량, 조금, 약간, 한 줌, 한줌, 약간의, 취향껏 같은 표현은 절대 사용하지 않는다. " +
                        "9. 같은 레시피 안에서는 단위를 일관되게 사용한다. " +
                        "10. 응답은 반드시 마크다운 없이 순수 JSON 배열([]) 형식으로만 출력한다. " +
                        "11. 'recipeName' 필드명을 반드시 지켜라.",
                ingredientsInfo
        );

        String responseJson = webClientBuilder.build()
                .post()
                .uri(apiUrl + "?key=" + apiKey)
                .bodyValue(Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Gemini API 오류 응답: {}", errorBody);
                                    return clientResponse.createException();
                                })
                )
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidate = root.path("candidates");
            if (!candidate.isArray() || candidate.isEmpty()) {
                throw new ExternalServiceException("AI 추천 결과가 비어 있습니다.");
            }

            String contentText = candidate.get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
            if (!StringUtils.hasText(contentText)) {
                throw new ExternalServiceException("AI 추천 결과 본문이 비어 있습니다.");
            }

            contentText = contentText.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            List<RecipeRecommendationDto> recipes = objectMapper.readValue(contentText, new TypeReference<List<RecipeRecommendationDto>>() {});
            recipeIngredientPolicyService.normalizeAndValidateRecommendations(recipes);
            return recipes;

        } catch (ExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패", e);
            throw new ExternalServiceException("AI 레시피 파싱 중 오류 발생", e);
        }
    }
}