package com.mohemeokji.mohemeokji.domain.imageanalyze.exception;

import com.mohemeokji.mohemeokji.global.exception.GlobalException;

public class ImageAnalyzeException extends GlobalException {

    public ImageAnalyzeException(ImageAnalyzeErrorCode errorCode) {
        super(errorCode);
    }
}