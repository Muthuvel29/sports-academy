package com.example.sportacademy.service;

import com.example.sportacademy.entity.Admin;
import com.example.sportacademy.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create admin account
    public Admin createAdmin(String email, String password) {

        if (adminRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Admin email already exists"
            );
        }

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));

        return adminRepository.save(admin);
    }

    // Admin login
    public Admin loginAdmin(String email, String password) {

        Admin admin = adminRepository.findByEmail(email)
                .orElse(null);

        if (admin != null &&
                passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }

        return null;
    }
}

