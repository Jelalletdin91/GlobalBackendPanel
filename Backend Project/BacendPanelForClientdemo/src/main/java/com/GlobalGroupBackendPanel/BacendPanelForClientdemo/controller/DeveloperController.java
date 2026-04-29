package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    private final CompanyRepository companyRepository;

    public DeveloperController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/companies")
    public String companies(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "Kimlik/developer-companies";
    }

    @GetMapping("/company/{id}/enter")
    public String enterCompany(@PathVariable Long id, HttpSession session) {
        session.setAttribute("DEV_COMPANY_ID", id);
        return "redirect:/";
    }
}