package com.example.naverLogin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/custom-login")
public class NaverOAuthController {
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    @GetMapping("/naver")
    public void naverLogin(HttpServletResponse response) throws IOException {
        String authUrl = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&state=" + UUID.randomUUID();
        
        response.sendRedirect(authUrl);
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<String> naverCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request) throws IOException {
        
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://nid.naver.com/oauth2.0/token?" +
                "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&state=" + state;

        ResponseEntity<String> tokenResponse = restTemplate.getForEntity(tokenUrl, String.class);
        JsonNode tokenNode = new ObjectMapper().readTree(tokenResponse.getBody());
        String accessToken = tokenNode.get("access_token").asText();

        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        JsonNode userNode = new ObjectMapper().readTree(userInfoResponse.getBody());
        String email = userNode.path("response").path("email").asText();
        String name = userNode.path("response").path("name").asText();
        log.info("Email: {}, Name: {}", email, name);

        OAuth2User principal = new DefaultOAuth2User(
            List.of(new SimpleGrantedAuthority("ROLE_USER")),
            Map.of("name", name, "email", email),
            "email"
        );

        Authentication auth = new OAuth2AuthenticationToken(
            principal,
            principal.getAuthorities(),
            "naver"
        );
        
        log.info("auth: {}", auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        // 세션에 보안 컨텍스트 저장
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                SecurityContextHolder.getContext());
        
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/home")).build();
    }
}
