package com.wheredidwego.exception;

public class FriendRequestException extends AppException {
    public FriendRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
