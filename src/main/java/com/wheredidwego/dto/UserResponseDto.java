package com.wheredidwego.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long idCode;
    private final String email;
    private final String nickname;

    @Builder
    public UserResponseDto(Long idCode, String email, String nickname) {
        this.idCode = idCode;
        this.email = email;
        this.nickname = nickname;
    }
}
