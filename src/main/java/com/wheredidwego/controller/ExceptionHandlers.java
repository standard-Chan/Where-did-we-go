package com.wheredidwego.controller;

import com.wheredidwego.dto.ErrorResponse;
import com.wheredidwego.exception.AppException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden // swagger 충돌 방지
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleFriendException(AppException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getErrorCode(),
                e.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
