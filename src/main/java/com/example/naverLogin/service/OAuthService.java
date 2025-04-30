package com.example.naverLogin.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final RestTemplate restTemplate;

    public OAuthService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String userInfoUri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
        
        // 액세스 토큰을 가져옵니다.
        String accessToken = userRequest.getAccessToken().getTokenValue();
        
        // 헤더에 액세스 토큰을 추가합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        // 사용자 정보를 가져옵니다.
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
        Map<String, Object> userAttributes = response.getBody();
        
        if (userAttributes == null) {
            throw new OAuth2AuthenticationException("Failed to fetch user attributes");
        }

        return new DefaultOAuth2User(
            Collections.singletonList(() -> "naver"),
            userAttributes,
            "response");
    }
}
