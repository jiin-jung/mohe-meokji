package com.mohemeokji.mohemeokji.domain.shopping.exception;

import com.mohemeokji.mohemeokji.global.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShoppingErrorCode implements BaseErrorCode {

    SHOPPING_ITEM_NOT_FOUND("SHOP_001", "해당 장보기 항목을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMPTY_SHORTAGE_LIST("SHOP_002", "장보기 목록에 추가할 결핍 재료가 없습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}