package com.wheredidwego.exception;

import lombok.Getter;

@Getter
public class FriendException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.FRIEND_NOT_FOUND;

    public FriendException(String message) {
        super(message);
    }
}
