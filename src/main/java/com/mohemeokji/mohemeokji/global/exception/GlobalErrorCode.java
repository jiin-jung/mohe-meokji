package com.mohemeokji.mohemeokji.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {

    INTERNAL_SERVER_ERROR("GLOBAL_500", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("GLOBAL_400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR("GLOBAL_400_VALID", "입력 값을 확인해주세요.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("GLOBAL_401", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("GLOBAL_403", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("GLOBAL_404", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_RESOURCE("GLOBAL_409", "이미 존재하는 리소스입니다.", HttpStatus.CONFLICT),
    EXTERNAL_SERVICE_ERROR("GLOBAL_502", "외부 서비스 오류가 발생했습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus status;
}