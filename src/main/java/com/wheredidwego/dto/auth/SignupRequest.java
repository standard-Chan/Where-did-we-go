package com.wheredidwego.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignupRequest {
    @NotNull
    String email;
    @NotNull
    String password;
    @NotNull
    String nickname;
}
