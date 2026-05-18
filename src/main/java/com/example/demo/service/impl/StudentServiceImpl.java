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
    @Transactional(readOnly = true)
    public boolean emailExistsForCreate(String email) {
        if (email == null) return false;
        String e = email.trim();
        if (e.isEmpty()) return false;
        return repo.existsByEmailIgnoreCase(e);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExistsForUpdate(Long id, String email) {
        if (email == null) return false;
        String e = email.trim();
        if (e.isEmpty()) return false;

        Student existing = findByIdOrThrow(id);
        return !existing.getEmail().equalsIgnoreCase(e) && repo.existsByEmailIgnoreCase(e);
    }

    @Override
    public Student create(StudentForm form) {
        String email = (form.getEmail() == null) ? null : form.getEmail().trim();

        if (email != null && repo.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        Student s = new Student(form.getName(), email);
        return repo.save(s);
    }

    @Override
    public Student update(Long id, StudentForm form) {
        Student existing = findByIdOrThrow(id);
        String newEmail = (form.getEmail() == null) ? null : form.getEmail().trim();

        if (newEmail != null
                && !existing.getEmail().equalsIgnoreCase(newEmail)
                && repo.existsByEmailIgnoreCase(newEmail)) {
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