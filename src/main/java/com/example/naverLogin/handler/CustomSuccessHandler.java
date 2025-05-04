package com.example.naverLogin.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Authentication authentication) throws IOException, ServletException {

        // 1. SecurityContext에 인증 정보가 있는지 확인
        SecurityContext context = SecurityContextHolder.getContext();
        log.info("context: {}", context);
        if (context.getAuthentication() == null) {
            context.setAuthentication(authentication); // 강제 설정
        }

        // 2. 세션에 직접 저장 (선택 사항)
        request.getSession().setAttribute(
            "SPRING_SECURITY_CONTEXT", context // 표준 세션 키
        );

        // 로그인 성공 후 처리 로직
        response.sendRedirect("/home");
    }
}