package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final CompanyContextService companyContextService;
    private final CurrentUserService currentUserService;

    public ProfileController(CompanyContextService companyContextService,
                             CurrentUserService currentUserService) {
        this.companyContextService = companyContextService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {

        Company company = companyContextService.getCurrentCompany();

        if (companyContextService.isDeveloper()) {
            model.addAttribute("companyName", company.getName());
            model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
            model.addAttribute("userName", "Developer");
            model.addAttribute("userRole", "DEVELOPER");
            model.addAttribute("userEmail", "-");

            return "Kimlik/profile";
        }

        AppUser currentUser = currentUserService.getCurrentUser();

        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
        model.addAttribute("userName", currentUser.getName());
        model.addAttribute("userRole", "YONETICI");
        model.addAttribute("userEmail", currentUser.getEmail());
        model.addAttribute("username", currentUser.getUsername());

        return "Kimlik/profile";
    }
}