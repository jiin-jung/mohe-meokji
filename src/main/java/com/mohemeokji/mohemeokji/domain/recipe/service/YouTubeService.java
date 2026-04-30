package com.mohemeokji.mohemeokji.domain.recipe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private static final Logger log = LoggerFactory.getLogger(YouTubeService.class);
    private static final Pattern NON_ALPHANUMERIC_OR_HANGUL = Pattern.compile("[^0-9a-zA-Z가-힣]");
    private static final List<String> COOKING_HINTS = List.of("레시피", "요리", "만들기", "cook", "cooking", "kitchen", "찌개", "국", "볶음", "조림");

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.youtube.api.key}")
    private String apiKey;

    @Value("${spring.youtube.api.url}")
    private String apiUrl;

    public String buildSearchQuery(String recipeName, String searchQuery) {
        if (StringUtils.hasText(searchQuery)) return searchQuery.trim();
        if (!StringUtils.hasText(recipeName)) return null;
        return recipeName.trim() + " 레시피";
    }

    public String buildSearchResultsUrl(String recipeName, String searchQuery) {
        String normalizedQuery = buildSearchQuery(recipeName, searchQuery);
        if (!StringUtils.hasText(normalizedQuery)) return null;
        return "https://www.youtube.com/results?search_query=" + URLEncoder.encode(normalizedQuery, StandardCharsets.UTF_8);
    }

    public String resolveRecipeVideoUrl(String recipeName, String searchQuery) {
        String normalizedQuery = buildSearchQuery(recipeName, searchQuery);
        if (!StringUtils.hasText(normalizedQuery)) return null;
        try {
            String requestUri = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("key", apiKey).queryParam("q", normalizedQuery)
                    .queryParam("part", "snippet").queryParam("maxResults", 5).queryParam("type", "video").toUriString();
            String response = webClientBuilder.build().get().uri(requestUri).retrieve().bodyToMono(String.class).block();
            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("items");
            if (items.isArray() && !items.isEmpty()) {
                for (JsonNode item : items) {
                    if (isRelevantRecipeVideo(item, recipeName, normalizedQuery)) {
                        String videoId = item.path("id").path("videoId").asText();
                        if (StringUtils.hasText(videoId)) return "https://www.youtube.com/watch?v=" + videoId;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("YouTube API 호출 실패: {}", e.getMessage());
        }
        return buildSearchResultsUrl(recipeName, normalizedQuery);
    }

    private boolean isRelevantRecipeVideo(JsonNode item, String recipeName, String searchQuery) {
        JsonNode snippet = item.path("snippet");
        String combinedText = String.join(" ", snippet.path("title").asText(""), snippet.path("description").asText(""), snippet.path("channelTitle").asText("")).toLowerCase(Locale.ROOT);
        String collapsedText = collapse(combinedText);
        if (!StringUtils.hasText(collapsedText)) return false;
        String collapsedRecipeName = collapse(recipeName);
        if (StringUtils.hasText(collapsedRecipeName) && collapsedText.contains(collapsedRecipeName)) return true;
        List<String> tokens = buildSignificantTokens(recipeName, searchQuery);
        long matched = tokens.stream().filter(collapsedText::contains).count();
        boolean hasCookingHint = COOKING_HINTS.stream().map(this::collapse).anyMatch(collapsedText::contains);
        int required = Math.min(2, tokens.size());
        return required > 0 && matched >= required && hasCookingHint;
    }

    private List<String> buildSignificantTokens(String recipeName, String searchQuery) {
        List<String> tokens = new ArrayList<>();
        appendTokens(tokens, recipeName);
        appendTokens(tokens, searchQuery);
        return tokens.stream().distinct().toList();
    }

    private void appendTokens(List<String> tokens, String source) {
        if (!StringUtils.hasText(source)) return;
        for (String token : source.split("\\s+")) {
            String collapsed = collapse(token);
            if (collapsed.length() >= 2 && !COOKING_HINTS.contains(token.toLowerCase(Locale.ROOT))) tokens.add(collapsed);
        }
    }

    private String collapse(String value) {
        if (!StringUtils.hasText(value)) return "";
        return NON_ALPHANUMERIC_OR_HANGUL.matcher(value.toLowerCase(Locale.ROOT)).replaceAll("");
    }
}