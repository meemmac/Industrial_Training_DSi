package com.example.demo.controller;

import com.example.demo.exception.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StudentNotFoundException.class)
    public String handleStudentNotFound(StudentNotFoundException ex, Model model) {
        model.addAttribute("title", "Not Found");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("hint", "Check the ID or go back to the students list.");
        model.addAttribute("backUrl", "/students");
        model.addAttribute("backLabel", "Back to Students");
        return "error/404";
    }

    // Optional: handle bad requests like invalid form business rules
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("title", "Bad Request");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("hint", "Please correct the input and try again.");
        model.addAttribute("backUrl", "/students");
        model.addAttribute("backLabel", "Back to Students");
        return "error/simple";
    }
}