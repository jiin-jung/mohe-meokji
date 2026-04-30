package com.mohemeokji.mohemeokji.global.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GlobalException(BaseErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}