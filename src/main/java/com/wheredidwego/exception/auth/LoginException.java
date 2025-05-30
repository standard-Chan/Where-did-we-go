package com.wheredidwego.exception.auth;

import com.wheredidwego.exception.AppException;
import com.wheredidwego.exception.ErrorCode;

public class LoginException extends AppException {
    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
