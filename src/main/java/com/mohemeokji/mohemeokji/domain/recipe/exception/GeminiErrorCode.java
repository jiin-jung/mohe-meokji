package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GeminiErrorCode implements BaseErrorCode {

    EMPTY_RESPONSE("GEMINI_001", "AI 추천 결과가 비어 있습니다.", HttpStatus.BAD_GATEWAY),
    EMPTY_RESPONSE_BODY("GEMINI_002", "AI 추천 결과 본문이 비어 있습니다.", HttpStatus.BAD_GATEWAY),
    PARSE_ERROR("GEMINI_003", "AI 레시피 파싱 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus status;
}