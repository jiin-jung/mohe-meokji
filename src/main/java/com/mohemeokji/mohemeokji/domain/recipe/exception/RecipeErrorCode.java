package com.mohemeokji.mohemeokji.domain.recipe.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode implements BaseErrorCode {

    RECIPE_NOT_FOUND("RECIPE_001", "해당 레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_RECIPE_NAME("RECIPE_002", "레시피 이름이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_SAVED_RECIPE("RECIPE_003", "이미 저장된 레시피입니다.", HttpStatus.CONFLICT),
    EMPTY_RECIPE_LIST("RECIPE_004", "추천 레시피가 비어 있습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_RECIPE_NAME("RECIPE_005", "레시피 이름이 비어 있습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_RECIPE_INGREDIENTS("RECIPE_006", "레시피 재료가 비어 있습니다.", HttpStatus.BAD_REQUEST),
    INVALID_INGREDIENT_NAME("RECIPE_007", "재료명이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_INGREDIENT_NAME("RECIPE_008", "사용할 수 없는 표현이 포함된 재료명입니다.", HttpStatus.BAD_REQUEST),
    INVALID_INGREDIENT_QUANTITY("RECIPE_009", "재료 수량은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_INGREDIENT_UNIT("RECIPE_010", "재료 단위가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    DISALLOWED_UNIT("RECIPE_011", "허용되지 않은 재료 단위입니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_INGREDIENTS("RECIPE_012", "추천을 위해 최소 1개 이상의 재료가 필요합니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}