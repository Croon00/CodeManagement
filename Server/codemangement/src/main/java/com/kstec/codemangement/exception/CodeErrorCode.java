package com.kstec.codemangement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CodeErrorCode implements ErrorCode {
    CODE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 코드입니다."),
    CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, 409, "코드가 이미 존재합니다."),
    INVALID_SEARCH_PARAMETERS(HttpStatus.BAD_REQUEST, 400, "잘못된 검색 파라미터입니다."),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
