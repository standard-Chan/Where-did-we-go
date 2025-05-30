package com.wheredidwego.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // Common
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),

    // Auth
    ALREADY_REGISTERED_EMAIL("이미 가입된 이메일입니다."),
    INVALID_EMAIL_FORMAT("유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD_LENGTH("비밀번호는 8자 이상 20자 이하로 입력해주세요."),
    INVALID_NICKNAME_FORMAT("유효하지 않은 닉네임입니다. "),

    // Photo Entry
    PHOTO_ENTRY_NOT_FOUND("해당 정보가 존재하지 않습니다."),
    NO_DELETE_PERMISSION("삭제 권한이 없습니다."),

    // Friend
    FRIEND_NOT_FOUND("친구를 찾을 수 없습니다."),
    IS_NOT_FRIEND("해당 유저와 친구가 아닙니다."),
    NOT_PERMISSION_TO_DELETE("친구 삭제 권한이 없습니다."),
    NOT_PERMISSION_TO_VIEW("친구의 정보를 볼 수 있는 권한이 없습니다."),
    INCORRECT_PERMISSION("권한 정보가 잘못 되었습니다."),

    // FriendRequest
    ALREADY_FRIEND("이미 친구상태인 유저입니다."),
    REQUEST_ALREADY_SENT("친구 요청을 이미 보냈습니다."),
    /*INVALID_TYPE_PARAM("잘못된 param TYPE이 전달되었습니다. (type은 sent 혹은 received 이어야 합니다.)"),*/
    FRIEND_REQUEST_NOT_FOUND("해당 id의 친구요청을 찾을 수 없습니다."),
    NO_PERMISSION_TO_DECIDE("친구 요청 결정 권한이 없습니다."),
    INVALID_STATUS("잘못된 STATUS 값입니다. STATUS는 REJECT, ACCEPTED 이어야 합니다.");



    private final String errorMessage;
}
