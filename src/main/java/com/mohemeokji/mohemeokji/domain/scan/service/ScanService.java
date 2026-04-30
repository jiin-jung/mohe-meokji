package com.mohemeokji.mohemeokji.domain.scan.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohemeokji.mohemeokji.domain.scan.dto.ScanIngredientDto;
import com.mohemeokji.mohemeokji.domain.scan.dto.ScanResultDto;
import com.mohemeokji.mohemeokji.domain.scan.exception.ScanErrorCode;
import com.mohemeokji.mohemeokji.domain.scan.exception.ScanException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScanService {

    private static final Logger log = LoggerFactory.getLogger(ScanService.class);

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${spring.gemini.api.key}")
    private String apiKey;

    @Value("${spring.gemini.api.url}")
    private String apiUrl;

    public ScanResultDto analyze(String dataUrl) {
        if (!StringUtils.hasText(dataUrl)) {
            throw new ScanException(ScanErrorCode.EMPTY_IMAGE_DATA);
        }

        String mimeType;
        String base64Data;

        if (dataUrl.startsWith("data:")) {
            int commaIdx = dataUrl.indexOf(',');
            if (commaIdx < 0) throw new ScanException(ScanErrorCode.INVALID_IMAGE_FORMAT);
            String header = dataUrl.substring(5, commaIdx);
            mimeType = header.contains(";") ? header.substring(0, header.indexOf(';')) : header;
            base64Data = dataUrl.substring(commaIdx + 1);
        } else {
            mimeType = "image/jpeg";
            base64Data = dataUrl;
        }

        validateBase64(base64Data);

        String prompt =
                "이 이미지에서 식재료를 모두 감지해줘. " +
                "반드시 아래 JSON 배열 형식으로만 응답해야 해: " +
                "[{ \"name\": \"재료명\", \"icon\": \"해당 재료를 나타내는 이모지 1개\", \"category\": \"육류/해산물/채소/조미료/기타 중 하나\" }]. " +
                "식재료가 아닌 물체는 포함하지 마. " +
                "마크다운 없이 순수 JSON 배열([])만 출력해.";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(
                                Map.of("inline_data", Map.of("mime_type", mimeType, "data", base64Data)),
                                Map.of("text", prompt)
                        )
                ))
        );

        String responseJson;
        try {
            responseJson = webClientBuilder.build()
                    .post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("Gemini Vision API 오류: {}", errorBody);
                                        return clientResponse.createException();
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Gemini Vision API 호출 실패", e);
            throw new ScanException(ScanErrorCode.SCAN_API_ERROR, "이미지 분석 중 오류가 발생했습니다.");
        }

        try {
            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new ScanException(ScanErrorCode.NO_INGREDIENT_DETECTED);
            }

            String contentText = candidates.get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            contentText = contentText.replaceAll("```json", "").replaceAll("```", "").trim();

            List<ScanIngredientDto> ingredients = objectMapper.readValue(
                    contentText, new TypeReference<List<ScanIngredientDto>>() {});
            return new ScanResultDto(ingredients);

        } catch (ScanException e) {
            throw e;
        } catch (Exception e) {
            log.error("이미지 분석 응답 파싱 실패", e);
            throw new ScanException(ScanErrorCode.SCAN_PARSE_ERROR, "이미지 분석 결과 파싱 중 오류가 발생했습니다.");
        }
    }

    private void validateBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64.replaceAll("\\s", ""));
        } catch (IllegalArgumentException e) {
            throw new ScanException(ScanErrorCode.INVALID_BASE64);
        }
    }
}