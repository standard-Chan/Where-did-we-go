package com.wheredidwego.exception;

public class PhotoEntryException extends AppException {
    public PhotoEntryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
