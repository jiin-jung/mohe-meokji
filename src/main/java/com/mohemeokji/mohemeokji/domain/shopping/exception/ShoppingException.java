package com.mohemeokji.mohemeokji.domain.shopping.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class ShoppingException extends GlobalException {

    public ShoppingException(ShoppingErrorCode errorCode) {
        super(errorCode);
    }

    public ShoppingException(ShoppingErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}