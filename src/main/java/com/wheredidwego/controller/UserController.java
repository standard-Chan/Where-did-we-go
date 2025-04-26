package com.wheredidwego.controller;

import com.wheredidwego.dto.UserResponseDto;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")
    public UserResponseDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.getUserResponseDtoByUserDetails(userDetails);
    }
}
