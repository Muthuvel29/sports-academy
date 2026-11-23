package com.example.sportacademy.controller;

import com.example.sportacademy.dto.LoginRequest;
import com.example.sportacademy.entity.Admin;
import com.example.sportacademy.service.AdminService;
import com.example.sportacademy.service.StudentService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class AdminController {

    private final AdminService adminService;
    private final StudentService studentService;

    public AdminController(AdminService adminService,
                           StudentService studentService) {
        this.adminService = adminService;
        this.studentService = studentService;
    }

    // Admin login
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(
            @RequestBody LoginRequest request,
            HttpSession session) {

        Admin admin = adminService.loginAdmin(
                request.getEmail(),
                request.getPassword()
        );

        if (admin == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        session.setAttribute("adminId", admin.getId());

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin login successful",
                        "email", admin.getEmail()
                )
        );
    }

    // Admin logout
    @PostMapping("/logout")
    public ResponseEntity<?> logoutAdmin(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                Map.of("message", "Admin logged out successfully")
        );
    }

    // Check whether admin is logged in
    @GetMapping("/me")
    public ResponseEntity<?> checkAdminSession(HttpSession session) {

        Object adminId = session.getAttribute("adminId");

        if (adminId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin login required"));
        }

        return ResponseEntity.ok(
                Map.of("message", "Admin is logged in")
        );
    }

    // Get student statistics
    @GetMapping("/statistics")
    public ResponseEntity<?> getStudentStatistics(HttpSession session) {

        // Check admin login
        if (session.getAttribute("adminId") == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Admin login required"));
        }

        Map<String, Long> statistics = Map.of(
                "totalStudents", studentService.getTotalStudentCount(),
                "kabaddi", studentService.getStudentCountBySport("Kabaddi"),
                "cricket", studentService.getStudentCountBySport("Cricket"),
                "football", studentService.getStudentCountBySport("Football"),
                "volleyball", studentService.getStudentCountBySport("Volleyball")
        );

        return ResponseEntity.ok(statistics);
    }
}

