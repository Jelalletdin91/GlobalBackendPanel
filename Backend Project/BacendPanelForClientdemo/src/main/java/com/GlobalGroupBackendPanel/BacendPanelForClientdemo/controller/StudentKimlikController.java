package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.KimlikServiceImpl;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.StudentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Student_kimlik")
public class StudentKimlikController {

    private StudentService studentService;

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
    public String save(@ModelAttribute("studentKimlik") StudentKimlik studentKimlik, Model model) {
        studentKimlik.setNotified60days(false);
        try {
            studentService.save(studentKimlik);
            return "redirect:/Student_kimlik/list";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("StudentKimlik", studentKimlik);
            model.addAttribute("errorMessage", "This kimlik number already exists. Please enter a different kimlik number.");
            return "Kimlik/student-kimlik-form";
        } catch (Exception e) {
            model.addAttribute("studentKimlik", studentKimlik);
            model.addAttribute("errorMessage", "Something went wrong while saving the record.");
            return "Kimlik/student-kimlik-form";
        }
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