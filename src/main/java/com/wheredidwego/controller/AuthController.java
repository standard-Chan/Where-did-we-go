package com.wheredidwego.controller;

import com.wheredidwego.entity.User;
import com.wheredidwego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
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

        try {
            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
            return ResponseEntity.ok().body(user.getNickname());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

//        // AuthenticationManager에게 인증 요청
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(username, password);
//
//        try {
//            // 여기서 → CustomAuthenticationProvider의 authenticate()가 실행됨
//            Authentication authentication = authenticationManager.authenticate(authToken);
//
//            // 인증 성공하면 JWT 발급
//            String token = jwtUtil.createToken(username);
//            return ResponseEntity.ok().body(Map.of("token", token));
//
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
//        }
    }

}
