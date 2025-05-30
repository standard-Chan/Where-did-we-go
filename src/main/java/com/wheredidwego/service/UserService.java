package com.wheredidwego.service;

import com.wheredidwego.dto.UserResponseDto;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.auth.SignupRequest;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.exception.auth.SignupException;
import com.wheredidwego.exception.auth.UserException;
import com.wheredidwego.repository.UserRepository;
import com.wheredidwego.util.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String nickname = signupRequest.getNickname();

        UserValidator.validateSignupInput(email, password, nickname);

        if (userRepository.existsUserByEmail(email)) {
            throw new SignupException(ErrorCode.ALREADY_REGISTERED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, nickname);
        userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public User findUserByUserDetails(UserDetails userDetails) {
        return findUserByEmail(userDetails.getUsername());
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND));
    }


    public UserResponseDto getUserResponseDtoByUserDetails(UserDetails userDetails) {
        User user = findUserByEmail(userDetails.getUsername());
        return UserResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }


}
