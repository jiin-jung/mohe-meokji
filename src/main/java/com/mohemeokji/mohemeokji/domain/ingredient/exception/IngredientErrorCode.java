package com.mohemeokji.mohemeokji.domain.ingredient.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum IngredientErrorCode implements BaseErrorCode {

    INGREDIENT_NOT_FOUND("INGR_001", "해당 재료를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_EXPIRY_DATE("INGR_002", "유통기한은 구매일보다 이전일 수 없습니다.", HttpStatus.BAD_REQUEST),
    INGREDIENT_NOT_FOUND_IN_STOCK("INGR_003", "재고에서 해당 재료를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus status;
}