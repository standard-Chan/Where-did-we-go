package com.wheredidwego;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        System.out.println("Start Web Server 'Where Did We Go?'");
    }

    @Bean
    public CommandLineRunner checkOauth(ClientRegistrationRepository repo) {
        return args -> {
            var google = repo.findByRegistrationId("google");
            System.out.println("🔍 구글 등록 여부: " + (google != null ? "✅ 등록됨" : "❌ 등록 안됨"));
        };
    }
}