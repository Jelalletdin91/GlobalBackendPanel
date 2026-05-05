package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Student_kimlik")
public class StudentKimlikController {

    private final StudentService studentService;
    private final CompanyContextService companyContextService;

    public StudentKimlikController(StudentService studentService,
                                   CompanyContextService companyContextService) {
        this.studentService = studentService;
        this.companyContextService = companyContextService;
    }

    private void addCompanyInfo(Model model) {
        Company company = companyContextService.getCurrentCompany();
        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<StudentKimlik> studentKimliks = studentService.search(keyword);

        addCompanyInfo(model);
        model.addAttribute("studentKimliks", studentKimliks);
        model.addAttribute("keyword", keyword);

        return "Kimlik/student-kimlik-list";
    }

    @GetMapping("/showForm")
    public String showForm(Model model) {
        addCompanyInfo(model);
        model.addAttribute("studentKimlik", new StudentKimlik());
        return "Kimlik/student-kimlik-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("studentKimlik") StudentKimlik studentKimlik, Model model) {
        try {
            studentService.save(studentKimlik);
            return "redirect:/Student_kimlik/list";
        } catch (Exception e) {
            addCompanyInfo(model);
            model.addAttribute("studentKimlik", studentKimlik);
            model.addAttribute("errorMessage", "Something went wrong while saving the record.");
            return "Kimlik/student-kimlik-form";
        }
    }

    @GetMapping("/showStudentKimlikForUpdate")
    public String update(@RequestParam("studentKimlikId") Long id, Model model) {
        addCompanyInfo(model);
        model.addAttribute("studentKimlik", studentService.findById(id));
        return "Kimlik/student-kimlik-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("studentKimlikId") Long id) {
        studentService.deleteById(id);
        return "redirect:/Student_kimlik/list";
    }
}