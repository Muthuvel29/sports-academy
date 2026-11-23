package com.example.sportacademy.dto;


public class StudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private String sport;

    public StudentResponse(Long id, String fullName,
                           String email, String sport) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.sport = sport;
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

    public String getSport() {
        return sport;
    }
}

