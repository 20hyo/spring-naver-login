package com.example.naverLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("principal: {}", principal);
        if (principal instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) principal;
            log.info("user attributes: {}", user.getAttributes());
            
            // 직접 속성에 접근 (NaverOAuthController에서 설정한 방식)
            Map<String, Object> userInfo = user.getAttributes();
            log.info("name: {}, email: {}", userInfo.get("name"), userInfo.get("email"));
            
            model.addAttribute("userInfo", userInfo);
        } else {
            model.addAttribute("userInfo", null);
        }
        return "home";
    }
}