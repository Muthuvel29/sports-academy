package com.example.sportacademy.config;

import com.example.sportacademy.repository.AdminRepository;
import com.example.sportacademy.service.AdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Bean
    public CommandLineRunner createFirstAdmin(
            AdminService adminService,
            AdminRepository adminRepository) {

        return args -> {

            if (adminEmail.isBlank() || adminPassword.isBlank()) {
                System.out.println("Admin setup skipped: credentials not configured.");
                return;
            }

            if (!adminRepository.existsByEmail(adminEmail)) {
                adminService.createAdmin(adminEmail, adminPassword);

                System.out.println("First admin account created successfully!");
            } else {
                System.out.println("Admin account already exists.");
            }
        };
    }
}

