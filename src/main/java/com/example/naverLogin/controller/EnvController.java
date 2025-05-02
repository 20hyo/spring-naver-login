package com.example.naverLogin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvController {
    @GetMapping("/env")
    public String getEnv() {
        return "NAVER_REDIRECT_URI: " + System.getProperty("NAVER_REDIRECT_URI");
    }
}
