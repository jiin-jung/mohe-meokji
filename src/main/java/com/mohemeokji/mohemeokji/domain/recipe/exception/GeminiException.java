package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class GeminiException extends GlobalException {

    public GeminiException(GeminiErrorCode errorCode) {
        super(errorCode);
    }

    public GeminiException(GeminiErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}