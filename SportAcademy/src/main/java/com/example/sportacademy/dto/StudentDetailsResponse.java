package com.example.sportacademy.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudentDetailsResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String sport;
    private LocalDate dateOfBirth;
    private LocalDateTime registrationDate;

    public StudentDetailsResponse() {
    }

    public StudentDetailsResponse(
            Long id,
            String fullName,
            String email,
            String phone,
            String sport,
            LocalDate dateOfBirth,
            LocalDateTime registrationDate) {

        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.sport = sport;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getSport() {
        return sport;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}
