package com.wheredidwego.security.oauth2;

import com.wheredidwego.domain.User;
import com.wheredidwego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        if (registrationId.equals("google")) {
            String email = oAuth2User.getAttribute("email");
            String nickname = oAuth2User.getAttribute("name");

            // DB에 사용자 정보 가져오기. 없으면 저장
            User user = userRepository.findUserByEmail(email)
                    .orElseGet(() -> userRepository.save(new User(email, null, nickname)));

            return new CustomOAuth2User(user);
        }
        else {
            throw new OAuth2AuthenticationException("해당 OAuth2 인증은 등록되지 않았습니다. google OAuth2 인증으로 처리해주세요.");
        }
    }
}

