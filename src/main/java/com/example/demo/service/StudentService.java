package com.example.demo.service;

import com.example.demo.dto.StudentForm;
import com.example.demo.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();
    Student findByIdOrThrow(Long id);

    Student create(StudentForm form);
    Student update(Long id, StudentForm form);

    void delete(Long id);

    StudentForm toForm(Student student);
}