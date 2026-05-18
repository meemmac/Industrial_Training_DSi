package com.example.demo.controller;

import com.example.demo.dto.StudentForm;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", service.findAll());
        return "students/list";
    }

    // DETAILS
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Student student = service.findByIdOrThrow(id);
        model.addAttribute("student", student);
        return "students/details";
    }

    // CREATE - show form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("studentForm", new StudentForm());
        return "students/form";
    }

    // CREATE - submit
    @PostMapping
    public String create(@Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "students/form";
        }
        service.create(form);
        return "redirect:/students";
    }

    // UPDATE - show form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student student = service.findByIdOrThrow(id);

        model.addAttribute("mode", "edit");
        model.addAttribute("studentId", id);
        model.addAttribute("studentForm", service.toForm(student));

        return "students/form";
    }

    // UPDATE - submit
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("studentForm") StudentForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("studentId", id);
            return "students/form";
        }
        service.update(id, form);
        return "redirect:/students/" + id;
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/students";
    }
}