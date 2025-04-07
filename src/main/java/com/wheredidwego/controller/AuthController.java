package com.wheredidwego.controller;

import com.wheredidwego.entity.User;
import com.wheredidwego.repository.UserRepository;
import com.wheredidwego.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String nickname = request.get("nickname");

        if (userRepository.existsUserByEmail(email)) {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자입니다.");
        }

        userRepository.save(new User(email, password, nickname));
        return ResponseEntity.ok("로그인 성공!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // AuthenticationManager에 인증 요청
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        try {
            // 여기서 → CustomAuthenticationProvider의 authenticate()가 실행됨
            Authentication authentication = authenticationManager.authenticate(authToken);

            String authenticatedEmail = authentication.getName();
            // 인증 성공하면 JWT 발급
            String token = jwtUtil.createToken(authenticatedEmail);
            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

}
