package com.example.naverLogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class NaverLoginApplication {

	public static void main(String[] args) {
		try {
            Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .filename(".env")
                .load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            System.err.println("Failed to load .env: " + e.getMessage());
        }
		
		SpringApplication.run(NaverLoginApplication.class, args);
	}

}
