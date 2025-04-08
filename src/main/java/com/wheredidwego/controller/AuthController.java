package com.wheredidwego.controller;

import com.wheredidwego.entity.User;
import com.wheredidwego.repository.UserRepository;
import com.wheredidwego.service.UserService;
import com.wheredidwego.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String nickname = request.get("nickname");

        try {
            userService.signup(email, password, nickname);
            return ResponseEntity.ok("회원가입에 성공하였습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");


        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            // jwt token 발급
            User user = (User) authentication.getPrincipal();
            String token = jwtUtil.createToken(user.getEmail());
            return ResponseEntity.ok().body(Map.of("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }

}
