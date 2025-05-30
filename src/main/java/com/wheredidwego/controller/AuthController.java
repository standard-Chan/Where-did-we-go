package com.wheredidwego.controller;

import com.wheredidwego.domain.User;
import com.wheredidwego.dto.auth.SignupRequest;
import com.wheredidwego.service.UserService;
import com.wheredidwego.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String nickname = signupRequest.getNickname();

        userService.signup(email, password, nickname);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String email = request.get("email");
        String password = request.get("password");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            // jwt token 발급
            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.createToken(user.getEmail());

            // cookie 생성
            Cookie cookie = jwtUtil.createCookie(token);
            response.addCookie(cookie);

            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", "NULL");
        cookie.setPath("/"); // 전체 경로 설정
        //cookie.setHttpOnly(true); // 원래 쿠키가 HttpOnly면 유지
        //cookie.setSecure(true); // (선택) 원래 Secure면 유지
        cookie.setMaxAge(0); // 유효기간 0초로 만료 설정
        response.addCookie(cookie);

        return ResponseEntity.ok().body("로그아웃 완료");
    }

}
