package com.mohemeokji.mohemeokji.domain.imageanalyze.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageAnalyzeErrorCode implements BaseErrorCode {

    EMPTY_IMAGE("IMAGE_ANALYZE_001", "이미지 파일이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_FORMAT("IMAGE_ANALYZE_002", "지원하지 않는 이미지 형식입니다. (jpeg, png, webp, heic만 허용)", HttpStatus.BAD_REQUEST),
    IMAGE_TOO_LARGE("IMAGE_ANALYZE_003", "이미지 파일 크기가 10MB를 초과합니다.", HttpStatus.BAD_REQUEST),
    ANALYSIS_FAILED("IMAGE_ANALYZE_004", "이미지 분석 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    NO_INGREDIENT_DETECTED("IMAGE_ANALYZE_005", "이미지에서 식재료를 감지하지 못했습니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    INVALID_AI_RESPONSE("IMAGE_ANALYZE_006", "AI 응답을 파싱하는 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus status;
}