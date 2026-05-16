package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.UserRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
@PreAuthorize("hasRole('YONETICI')")
public class SettingsController {

    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CompanyContextService companyContextService;

    public SettingsController(CurrentUserService currentUserService,
                              PasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              CompanyContextService companyContextService) {
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.companyContextService=companyContextService;
    }

    private void addCompanyInfo(Model model) {
        Company company = companyContextService.getCurrentCompany();
        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));
    }

    @GetMapping
    public String settingsPage(Model model) {
        AppUser appUser = currentUserService.getCurrentUser();

        addCompanyInfo(model);
        model.addAttribute("username", "Yonetici");
        model.addAttribute("userRole", "Manager");
        model.addAttribute("user", appUser);
        return "Kimlik/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        AppUser user = currentUserService.getCurrentUser();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Current password is incorrect.");
            return "Kimlik/settings";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "New passwords do not match.");
            return "Kimlik/settings";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Password must be at least 6 characters.");
            return "Kimlik/settings";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("successMessage", "Password changed successfully.");

        return "Kimlik/settings";
    }
}