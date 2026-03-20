package com.mohemeokji.mohemeokji.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohemeokji.mohemeokji.domain.Ingredient;
import com.mohemeokji.mohemeokji.dto.RecipeRecommendationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.gemini.api.key}")
    private String apiKey;

    @Value("${spring.gemini.api.url}")
    private String apiUrl;

    public List<RecipeRecommendationDto> getRecipeRecommendations(List<Ingredient> ingredients) {
        String ingredientsInfo = ingredients.stream()
                .map(i -> i.getName() + "(" + i.getCategory() + "):" + i.getQuantity() + i.getUnit())
                .collect(Collectors.joining(","));

        String prompt = String.format(
                "내 냉장고 재료: [%s]. " +
                        "1. 이 재료들로 만들 수 있는 한국 요리 레시피 3~5개를 추천해줘. " +
                        "2. JSON 구조는 다음과 같아야 해: " +
                        "[{ \"recipeName\": \"요리명\", \"description\": \"설명\", \"ytSearchQuery\": \"요리명 + 레시피\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\", \"ingredients\": [{ \"name\": \"재료명\", \"quantityPerServing\": 0.0, \"unit\": \"단위\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\" }], \"maxServings\": 2, \"steps\": [\"1단계\", \"2단계\"] }] " +
                        "3. 응답은 반드시 마크다운 없이 순수 JSON 배열([]) 형식으로만 출력해. " +
                        "4. 'recipeName' 필드명을 반드시 지켜줘.",
                ingredientsInfo
        );

        // API 호출
        String responseJson = webClientBuilder.build()
                .post()
                .uri(apiUrl + "?key=" + apiKey)
                .bodyValue(Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.isError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> {
                                System.err.println("[DEBUG_LOG] Gemini API Error Response: " + errorBody);
                                System.err.println("[DEBUG_LOG] Full URL: " + apiUrl + "?key=" + apiKey);
                                return clientResponse.createException();
                            });
                })
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(responseJson);

            // AI의 응답 텍스트 추출
            String contentText = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // 마크다운 태그 제거 및 공백 정리
            contentText = contentText.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            // 핵심 수정: 단일 객체가 아닌 List(배열)로 파싱해야 함
            return objectMapper.readValue(contentText, new TypeReference<List<RecipeRecommendationDto>>() {});

        } catch (Exception e) {
            // 로깅을 추가하면 디버깅이 훨씬 편해집니다.
            System.err.println("AI 응답 파싱 실패: " + e.getMessage());
            throw new RuntimeException("AI 레시피 파싱 중 오류 발생", e);
        }
    }
}