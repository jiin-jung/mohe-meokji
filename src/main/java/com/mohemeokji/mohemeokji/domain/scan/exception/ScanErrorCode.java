package com.mohemeokji.mohemeokji.domain.scan.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScanErrorCode implements BaseErrorCode {

    EMPTY_IMAGE_DATA("SCAN_001", "이미지 데이터가 비어 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_FORMAT("SCAN_002", "올바르지 않은 이미지 형식입니다.", HttpStatus.BAD_REQUEST),
    INVALID_BASE64("SCAN_003", "유효하지 않은 base64 이미지입니다.", HttpStatus.BAD_REQUEST),
    SCAN_API_ERROR("SCAN_004", "이미지 분석 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY),
    NO_INGREDIENT_DETECTED("SCAN_005", "이미지에서 식재료를 찾지 못했습니다.", HttpStatus.BAD_GATEWAY),
    SCAN_PARSE_ERROR("SCAN_006", "이미지 분석 결과 파싱 중 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus status;
}