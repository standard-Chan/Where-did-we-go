package com.wheredidwego.security.oauth2;

import com.wheredidwego.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        // jwt 생성
        String token = jwtUtil.createToken(oAuth2User.getUser().getEmail());

        // redirect uri. session에서 추출 (client에서 query로 보내준 uri)
        String redirectUri = (String) request.getSession().getAttribute("redirect_uri");
        if (redirectUri == null) {
            redirectUri = "http://localhost:8080/success";
        }

        // cookie 생성
        Cookie cookie = jwtUtil.createCookie(token);
        response.addCookie(cookie);

        // 프론트로 리다이렉트하면서 파라미터로 jwt 전달
        response.sendRedirect(redirectUri);
    }
}
