package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class StudentForm {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Pattern(
            regexp = "^[A-Za-z]+(?:[ .][A-Za-z]+)*$",
            message = "Name must start with letters and may contain only letters separated by a single space or dot."
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "Email must be at most 150 characters")
    private String email;

    public StudentForm() {}

    public StudentForm(String name, String email) {
        setName(name);
        setEmail(email);
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = normalizeSpaces(name);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = (email == null) ? null : email.trim();
    }

    private static String normalizeSpaces(String s) {
        if (s == null) return null;
        return s.trim().replaceAll("\\s+", " ");
    }
}