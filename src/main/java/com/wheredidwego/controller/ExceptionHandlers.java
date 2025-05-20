package com.wheredidwego.controller;

import com.wheredidwego.dto.ErrorResponse;
import com.wheredidwego.exception.FriendException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(FriendException.class)
    public ResponseEntity<ErrorResponse> handleFriendException(FriendException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getErrorCode(),
                e.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
