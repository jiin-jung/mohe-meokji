package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class CookException extends GlobalException {

    public CookException(CookErrorCode errorCode) {
        super(errorCode);
    }

    public CookException(CookErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}