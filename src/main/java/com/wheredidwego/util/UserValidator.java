package com.wheredidwego.util;

import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.auth.SignupException;

import java.util.regex.Pattern;

public class UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_-]]");

    public static void validateSignupInput(String email, String password, String nickname) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new SignupException(ErrorCode.INVALID_EMAIL_FORMAT);
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new SignupException(ErrorCode.INVALID_PASSWORD_LENGTH);
        }

        if (nickname.isEmpty() || !NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new SignupException(ErrorCode.INVALID_NICKNAME_FORMAT);
        }
    }
}
