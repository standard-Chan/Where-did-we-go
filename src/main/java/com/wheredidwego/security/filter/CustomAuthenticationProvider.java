package com.wheredidwego.security.filter;

import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("잘못된 이메일입니다."));

        // 잘못된 비밀번호 처리 및 OAuth2 유저의 password == null 보안처리
        if (rawPassword==null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }

        return new UsernamePasswordAuthenticationToken(user, null, List.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}