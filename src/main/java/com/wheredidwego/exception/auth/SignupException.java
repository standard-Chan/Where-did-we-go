package com.wheredidwego.exception.auth;

import com.wheredidwego.exception.AppException;
import com.wheredidwego.exception.ErrorCode;

public class SignupException extends AppException {
    public SignupException(ErrorCode errorCode) {
        super(errorCode);
    }
}
