package com.mohemeokji.mohemeokji.domain.ingredient.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class IngredientException extends GlobalException {

    public IngredientException(IngredientErrorCode errorCode) {
        super(errorCode);
    }

    public IngredientException(IngredientErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}