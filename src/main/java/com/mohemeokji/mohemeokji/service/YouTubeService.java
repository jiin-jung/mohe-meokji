package com.mohemeokji.mohemeokji.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.youtube.api.key}")
    private String apiKey;

    @Value("${spring.youtube.api.url}")
    private String apiUrl;

    public String getTopVideoUrl(String searchQuery) {
        try {
            String response = webClientBuilder.build()
                    .get()
                    .uri(apiUrl + "?key=" + apiKey + "&q=" + searchQuery + "&part=snippet&maxResults=1&type=video")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("items");

            if (items.isArray() && !items.isEmpty()) {
                String videoId = items.get(0).path("id").path("videoId").asText();
                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            System.err.println("YouTube API 호출 실패: " + e.getMessage());
        }
        return null;
    }
}
