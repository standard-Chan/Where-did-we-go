package com.wheredidwego.exception;

import lombok.Getter;

public class AppException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
