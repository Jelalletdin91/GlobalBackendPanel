package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@PreAuthorize("hasRole('YONETICI')")
public class SettingsController {

    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;

    public SettingsController(CurrentUserService currentUserService, PasswordEncoder passwordEncoder){
        this.currentUserService=currentUserService;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping
    public String settingsPage(Model model){
        AppUser appUser = currentUserService.getCurrentUser();

        model.addAttribute("user", appUser);

        return "Kimlik/settings";
    }


}
