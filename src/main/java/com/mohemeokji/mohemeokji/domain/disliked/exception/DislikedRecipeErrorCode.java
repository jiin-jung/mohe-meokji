package com.mohemeokji.mohemeokji.domain.disliked.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DislikedRecipeErrorCode implements BaseErrorCode {

    DISLIKED_RECIPE_NOT_FOUND("DISLIKE_001", "해당 싫어요 레시피를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_RECIPE_NAME("DISLIKE_002", "레시피 이름이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_DISLIKE("DISLIKE_003", "이미 싫어요 처리한 레시피입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}