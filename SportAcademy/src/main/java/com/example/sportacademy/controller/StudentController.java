

package com.example.sportacademy.controller;

import com.example.sportacademy.dto.LoginRequest;
import com.example.sportacademy.dto.StudentResponse;
import com.example.sportacademy.dto.StudentDetailsResponse;
import com.example.sportacademy.entity.Student;
import com.example.sportacademy.service.StudentService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(
        origins = {
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        },
        allowCredentials = "true"
)
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Student registration
    @PostMapping("/register")
    public StudentResponse registerStudent(@RequestBody Student student) {

        Student savedStudent = studentService.registerStudent(student);

        return new StudentResponse(
                savedStudent.getId(),
                savedStudent.getFullName(),
                savedStudent.getEmail(),
                savedStudent.getSport()
        );
    }

    // Student login
    @PostMapping("/login")
    public StudentResponse loginStudent(@RequestBody LoginRequest request) {

        Student student = studentService.login(
                request.getEmail(),
                request.getPassword()
        );

        if (student == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        return new StudentResponse(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getSport()
        );
    }

    // Reset student password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody LoginRequest request) {

        boolean updated = studentService.resetPassword(
                request.getEmail(),
                request.getPassword()
        );

        if (!updated) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Student email not found"
            );
        }

        return "Password updated successfully!";
    }

    // Get all student details - Admin only
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllStudents(HttpSession session) {

        if (session.getAttribute("adminId") == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Admin login required"
                    ));
        }

        List<StudentDetailsResponse> students =
                studentService.getAllStudentDetails();

        return ResponseEntity.ok(students);
    }

    // Update student details - Admin only
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent,
            HttpSession session) {

        // Check admin login
        if (session.getAttribute("adminId") == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Admin login required"
                    ));
        }

        try {

            StudentDetailsResponse updated =
                    studentService.updateStudent(id, updatedStudent);

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }
    // Delete student - Admin only
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteStudent(
            @PathVariable Long id,
            HttpSession session) {

        // Check admin login
        if (session.getAttribute("adminId") == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Admin login required"
                    ));
        }

        boolean deleted = studentService.deleteStudent(id);

        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message",
                            "Student not found"
                    ));
        }

        return ResponseEntity.ok(
                Map.of("message", "Student deleted successfully")
        );
    }
}


