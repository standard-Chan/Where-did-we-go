package com.wheredidwego.exception.auth;

import com.wheredidwego.exception.AppException;
import com.wheredidwego.exception.ErrorCode;

public class UserException extends AppException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
