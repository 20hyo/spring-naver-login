package com.example.naverLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

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
        if (principal instanceof OAuth2User) {
            OAuth2User user = (OAuth2User) principal;
            log.info("user: {}", user.getAttributes().get("response"));
            model.addAttribute("userInfo", user.getAttributes().get("response"));
        } else {
            model.addAttribute("userInfo", null);
        }
        return "home";
    }
}