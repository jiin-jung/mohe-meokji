package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class RecipeException extends GlobalException {

    public RecipeException(RecipeErrorCode errorCode) {
        super(errorCode);
    }

    public RecipeException(RecipeErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}