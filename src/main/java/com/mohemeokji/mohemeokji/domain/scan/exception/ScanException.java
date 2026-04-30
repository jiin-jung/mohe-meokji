package com.mohemeokji.mohemeokji.domain.scan.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class ScanException extends GlobalException {

    public ScanException(ScanErrorCode errorCode) {
        super(errorCode);
    }

    public ScanException(ScanErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}