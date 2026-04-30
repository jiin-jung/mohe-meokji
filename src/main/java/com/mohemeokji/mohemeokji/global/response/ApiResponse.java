package com.mohemeokji.mohemeokji.global.response;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;

public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        T result
) {

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(true, "COMMON_200", "요청에 성공했습니다.", result);
    }

    public static <T> ApiResponse<T> failure(BaseErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> failure(BaseErrorCode errorCode, String message) {
        return new ApiResponse<>(false, errorCode.getCode(), message, null);
    }
}