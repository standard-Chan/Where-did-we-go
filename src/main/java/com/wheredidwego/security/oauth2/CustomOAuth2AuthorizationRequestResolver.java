package com.wheredidwego.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/*
    /oauth2/authorization/{registrationId} 로 요청을 보낼 때, 이 request를 가로채서 로직을 처리할 수 있는 메서드

 */

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    // resolver는 Request를 java 객체를 만들어주는 기능.

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository repo) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(request);

        return customizeAuthorizationRequest(request, originalRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        // registrationId는 OAuth2의 Provider ex. google, kakao 를 구별하는 id
        OAuth2AuthorizationRequest originalRequest = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(request, originalRequest);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request, OAuth2AuthorizationRequest originalRequest) {
        // redirect 주소를 query string에 넣지 않은 경우
        if (originalRequest == null) {
            return null;
        }
        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri != null) {
            // redirect uri를 session에 저장.
            request.getSession().setAttribute("redirect_uri", redirectUri);
        }

        return originalRequest;
    }
}
