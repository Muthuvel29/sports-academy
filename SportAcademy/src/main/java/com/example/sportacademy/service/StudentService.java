
package com.example.sportacademy.service;

import com.example.sportacademy.entity.Student;
import com.example.sportacademy.dto.StudentDetailsResponse;
import com.example.sportacademy.repository.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Student registration
    public Student registerStudent(Student student) {

        if (emailExists(student.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        student.setPassword(
                passwordEncoder.encode(student.getPassword())
        );

        return studentRepository.save(student);
    }

    // Check email
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }

    // Student login
    public Student login(String email, String rawPassword) {

        Student student = studentRepository.findByEmail(email)
                .orElse(null);

        if (student == null) {
            return null;
        }

        if (passwordEncoder.matches(rawPassword, student.getPassword())) {
            return student;
        }

        return null;
    }

    // Reset password
    public boolean resetPassword(String email, String newPassword) {

        Student student = studentRepository.findByEmail(email)
                .orElse(null);

        if (student == null) {
            return false;
        }

        student.setPassword(passwordEncoder.encode(newPassword));

        studentRepository.save(student);

        return true;
    }

    // Count students by sport
    public long getStudentCountBySport(String sport) {
        return studentRepository.countBySport(sport);
    }

    // Total student count
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    // Get all students without exposing passwords
    public List<StudentDetailsResponse> getAllStudentDetails() {

        return studentRepository.findAll()
                .stream()
                .map(student -> new StudentDetailsResponse(
                        student.getId(),
                        student.getFullName(),
                        student.getEmail(),
                        student.getPhone(),
                        student.getSport(),
                        student.getDateOfBirth(),
                        student.getRegistrationDate()
                ))
                .toList();
    }

    // Update student details by admin
    public StudentDetailsResponse updateStudent(
            Long id, Student updatedStudent) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        // Check whether email belongs to another student
        Student existingStudent = studentRepository
                .findByEmail(updatedStudent.getEmail())
                .orElse(null);

        if (existingStudent != null &&
                !existingStudent.getId().equals(id)) {

            throw new IllegalArgumentException(
                    "Email already registered");
        }

        student.setFullName(updatedStudent.getFullName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhone(updatedStudent.getPhone());
        student.setSport(updatedStudent.getSport());
        student.setDateOfBirth(updatedStudent.getDateOfBirth());

        Student savedStudent = studentRepository.save(student);

        return new StudentDetailsResponse(
                savedStudent.getId(),
                savedStudent.getFullName(),
                savedStudent.getEmail(),
                savedStudent.getPhone(),
                savedStudent.getSport(),
                savedStudent.getDateOfBirth(),
                savedStudent.getRegistrationDate()
        );
    }
    // Delete student by admin
    public boolean deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElse(null);

        if (student == null) {
            return false;
        }

        studentRepository.delete(student);

        return true;
    }
}



