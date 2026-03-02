package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Student_kimlik")
public class StudentKimlikController {

    private final StudentService studentService;

    public StudentKimlikController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<StudentKimlik> studentKimliks = studentService.findAll();
        model.addAttribute("studentKimliks", studentKimliks);
        return "Kimlik/student-kimlik-list";
    }

    @GetMapping("/showForm")
    public String showForm(Model model) {
        StudentKimlik studentKimlik = new StudentKimlik();
        model.addAttribute("studentKimlik", studentKimlik);
        return "Kimlik/student-kimlik-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("studentKimlik") StudentKimlik studentKimlik) {
        studentKimlik.setNotified60days(false);
        studentService.save(studentKimlik);
        return "redirect:/Student_kimlik/list";
    }

    @GetMapping("/showStudentKimlikForUpdate")
    public String update(@RequestParam("studentKimlikId") Long id, Model model) {
        StudentKimlik studentKimlik = studentService.findById(id);
        model.addAttribute("studentKimlik", studentKimlik);
        return "Kimlik/student-kimlik-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("studentKimlikId") Long id) {
        studentService.deleteById(id);
        return "redirect:/Student_kimlik/list";
    }
}