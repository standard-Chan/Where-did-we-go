package com.wheredidwego.exception;

import lombok.Getter;

@Getter
public class FriendException extends RuntimeException {
    private final ErrorCode errorCode;

    public FriendException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

}
