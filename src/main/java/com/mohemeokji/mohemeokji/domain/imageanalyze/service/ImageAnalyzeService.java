package com.mohemeokji.mohemeokji.domain.imageanalyze.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohemeokji.mohemeokji.domain.imageanalyze.dto.DetectedIngredientDto;
import com.mohemeokji.mohemeokji.domain.imageanalyze.dto.ImageAnalyzeResDto;
import com.mohemeokji.mohemeokji.domain.imageanalyze.exception.ImageAnalyzeErrorCode;
import com.mohemeokji.mohemeokji.domain.imageanalyze.exception.ImageAnalyzeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageAnalyzeService {

    private static final Logger log = LoggerFactory.getLogger(ImageAnalyzeService.class);

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L; // 10MB
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/heic"
    );

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.gemini.api.key}")
    private String apiKey;

    @Value("${spring.gemini.api.url}")
    private String apiUrl;

    public ImageAnalyzeResDto analyzeImage(MultipartFile image) {
        validateImage(image);

        String base64Image = encodeToBase64(image);
        String mimeType = image.getContentType();

        String responseJson = callGeminiVisionApi(base64Image, mimeType);
        List<DetectedIngredientDto> detectedIngredients = parseResponse(responseJson);

        if (detectedIngredients.isEmpty()) {
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.NO_INGREDIENT_DETECTED);
        }

        return new ImageAnalyzeResDto(detectedIngredients);
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.EMPTY_IMAGE);
        }
        if (!ALLOWED_MIME_TYPES.contains(image.getContentType())) {
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.UNSUPPORTED_FORMAT);
        }
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.IMAGE_TOO_LARGE);
        }
    }

    private String encodeToBase64(MultipartFile image) {
        try {
            return Base64.getEncoder().encodeToString(image.getBytes());
        } catch (IOException e) {
            log.error("이미지 인코딩 실패", e);
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.ANALYSIS_FAILED);
        }
    }

    private String callGeminiVisionApi(String base64Image, String mimeType) {
        String prompt =
                "이 이미지에서 식재료를 모두 감지해줘. " +
                "반드시 아래 JSON 배열 형식으로만 응답해야 해: " +
                "[{ \"name\": \"재료명\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\", \"estimatedQuantity\": \"숫자\", \"unit\": \"g/kg/ml/L/개/알/장/줄기/쪽/모/단/큰술/작은술/컵 중 하나\" }]. " +
                "식재료가 아닌 물체는 포함하지 마. " +
                "마크다운 없이 순수 JSON 배열([])만 출력해.";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("inline_data", Map.of(
                                        "mime_type", mimeType,
                                        "data", base64Image
                                )),
                                Map.of("text", prompt)
                        )
                ))
        );

        try {
            return webClientBuilder.build()
                    .post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Gemini Vision API 오류 응답: {}", errorBody);
                                        return clientResponse.createException();
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Gemini Vision API 호출 실패", e);
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.ANALYSIS_FAILED);
        }
    }

    private List<DetectedIngredientDto> parseResponse(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidates = root.path("candidates");

            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new ImageAnalyzeException(ImageAnalyzeErrorCode.NO_INGREDIENT_DETECTED);
            }

            String contentText = candidates.get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            if (!StringUtils.hasText(contentText)) {
                throw new ImageAnalyzeException(ImageAnalyzeErrorCode.NO_INGREDIENT_DETECTED);
            }

            contentText = contentText
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            return objectMapper.readValue(contentText, new TypeReference<List<DetectedIngredientDto>>() {});

        } catch (ImageAnalyzeException e) {
            throw e;
        } catch (Exception e) {
            log.error("이미지 분석 AI 응답 파싱 실패", e);
            throw new ImageAnalyzeException(ImageAnalyzeErrorCode.INVALID_AI_RESPONSE);
        }
    }
}