package com.mohemeokji.mohemeokji.domain.user.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class UserException extends GlobalException {

    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(UserErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}