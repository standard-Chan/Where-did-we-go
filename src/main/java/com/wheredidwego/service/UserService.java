package com.wheredidwego.service;

import com.wheredidwego.dto.UserResponseDto;
import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import com.wheredidwego.util.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String email, String password, String nickname) {

        UserValidator.validateSignupInput(email, password);

        if (userRepository.existsUserByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, nickname);
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 이메일입니다."));
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 ID 입니다."));
    }

    public UserResponseDto getUserResponseDto(Long userId) {
        User user = findUserById(userId);

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

}
