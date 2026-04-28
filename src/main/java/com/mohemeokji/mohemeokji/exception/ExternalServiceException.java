package com.mohemeokji.mohemeokji.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends BusinessException {

    public ExternalServiceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY);
        initCause(cause);
    }
}