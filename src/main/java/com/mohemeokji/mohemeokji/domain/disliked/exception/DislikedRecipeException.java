package com.mohemeokji.mohemeokji.domain.disliked.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class DislikedRecipeException extends GlobalException {

    public DislikedRecipeException(DislikedRecipeErrorCode errorCode) {
        super(errorCode);
    }

    public DislikedRecipeException(DislikedRecipeErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}