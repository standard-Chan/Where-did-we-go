package com.wheredidwego.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final User user;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "email", user.getEmail()
                , "nickname", user.getNickname());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 필요하면 ROLE_USER 등 추가
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

}
