package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        return "Kimlik/company-register";
    }

    @PostMapping("/register")
    public String registerCompany(
            @RequestParam String companyName,
            @RequestParam String ownerName,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "MONTHLY") String subscriptionPlan,
            @RequestParam(required = false) Long invitedByUserId,
            Model model
    ) {
        try {
            companyService.createCompanyWithOwner(
                    companyName,
                    ownerName,
                    email,
                    username,
                    password,
                    subscriptionPlan,
                    invitedByUserId
            );

            return "redirect:/loginPage?registered";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("companyName", companyName);
            model.addAttribute("ownerName", ownerName);
            model.addAttribute("email", email);
            model.addAttribute("username", username);
            model.addAttribute("subscriptionPlan", subscriptionPlan);
            model.addAttribute("invitedByUserId", invitedByUserId);

            return "Kimlik/company-register";
        }
    }
}