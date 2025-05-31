package com.wheredidwego.exception;

public class GeocodingException extends AppException {
    public GeocodingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
