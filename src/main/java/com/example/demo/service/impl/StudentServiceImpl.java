package com.example.demo.service.impl;

import com.example.demo.dto.StudentForm;
import com.example.demo.entity.Student;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    public StudentServiceImpl(StudentRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student create(StudentForm form) {
        if (repo.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + form.getEmail());
        }

        Student s = new Student(form.getName(), form.getEmail());
        return repo.save(s);
    }

    @Override
    public Student update(Long id, StudentForm form) {
        Student existing = findByIdOrThrow(id);

        String newEmail = form.getEmail();
        if (!existing.getEmail().equalsIgnoreCase(newEmail) && repo.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email already exists: " + newEmail);
        }

        existing.setName(form.getName());
        existing.setEmail(newEmail);

        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Student existing = findByIdOrThrow(id);
        repo.delete(existing);
    }

    @Override
    public StudentForm toForm(Student student) {
        return new StudentForm(student.getName(), student.getEmail());
    }
}