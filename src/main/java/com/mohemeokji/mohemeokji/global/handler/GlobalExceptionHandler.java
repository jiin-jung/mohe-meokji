package com.mohemeokji.mohemeokji.global.handler;

import com.mohemeokji.mohemeokji.global.exception.GlobalErrorCode;
import com.mohemeokji.mohemeokji.global.exception.GlobalException;
import com.mohemeokji.mohemeokji.global.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 모든 도메인 예외의 최상위 처리 포인트.
     * UserException, IngredientException, RecipeException, CookException,
     * GeminiException, ShoppingException, DislikedRecipeException, ScanException,
     * ImageAnalyzeException 등 GlobalException을 상속하는 모든 예외를 처리합니다.
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(GlobalException e) {
        HttpStatus status = e.getErrorCode().getStatus();
        if (status.is5xxServerError()) {
            log.error("[{}] {}", e.getErrorCode().getCode(), e.getMessage(), e);
        } else {
            log.warn("[{}] {}", e.getErrorCode().getCode(), e.getMessage());
        }
        return ResponseEntity
                .status(status)
                .body(ApiResponse.failure(e.getErrorCode(), e.getMessage()));
    }

    /**
     * @Valid, @Validated Bean Validation 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .distinct()
                .collect(Collectors.joining(", ")) + " 필드를 확인해주세요.";
        log.warn("[{}] {}", GlobalErrorCode.VALIDATION_ERROR.getCode(), message);
        return ResponseEntity
                .status(GlobalErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.failure(GlobalErrorCode.VALIDATION_ERROR, message));
    }

    /**
     * @Validated 경로/파라미터 단순 제약 위반
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("[{}] {}", GlobalErrorCode.VALIDATION_ERROR.getCode(), e.getMessage());
        return ResponseEntity
                .status(GlobalErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.failure(GlobalErrorCode.VALIDATION_ERROR, e.getMessage()));
    }

    /**
     * 처리되지 않은 예외 (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("[{}] 처리되지 않은 예외 발생", GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(), e);
        return ResponseEntity
                .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.failure(GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }
}