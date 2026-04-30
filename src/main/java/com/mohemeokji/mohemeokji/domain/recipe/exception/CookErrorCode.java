package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CookErrorCode implements BaseErrorCode {

    INVALID_RECIPE_INGREDIENT_QUANTITY("COOK_001", "레시피 재료 수량이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("COOK_002", "현재 재고로는 해당 레시피를 조리할 수 없습니다.", HttpStatus.BAD_REQUEST),
    EXCEEDED_MAX_SERVINGS("COOK_003", "요청 인분이 재고보다 많습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}