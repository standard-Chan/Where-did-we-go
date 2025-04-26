package com.wheredidwego.security.filter;

import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String authHeader = request.getHeader("Authorization");


        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // header에서 jwt 가져오기
            token = authHeader.substring(7);
        }
        else if (request.getCookies() != null) { // 쿠키에서 jwt 가져오기
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("access_token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        else { // Jwt가 없을 경우
            filterChain.doFilter(request, response);
            return;
        }

        // jwt 유효성 검사
        if (!jwtUtil.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //user detail 객체 생성
        CustomUserDetails userDetails = createUserDetailsFromJwtToken(token);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, List.of());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    protected CustomUserDetails createUserDetailsFromJwtToken(String token) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        return new CustomUserDetails(user);
    }
}

