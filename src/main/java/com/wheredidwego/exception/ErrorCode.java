package com.wheredidwego.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // Friend
    FRIEND_NOT_FOUND("친구를 찾을 수 없습니다."),
    IS_NOT_FRIEND("해당 유저와 친구가 아닙니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    NOT_PERMISSION("친구 삭제 권한이 없습니다.");


    private final String errorMessage;
}
