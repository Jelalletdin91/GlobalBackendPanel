package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CurrentUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final VorkerRepository vorkerRepository;
    private final ActivityLogRepository activityLogRepository;
    private final CurrentUserService currentUserService;

    public MainController(KimlikRepository kimlikRepository,
                          StudentRepository studentRepository,
                          VorkerRepository vorkerRepository,
                          ActivityLogRepository activityLogRepository,
                          CurrentUserService currentUserService) {

        this.kimlikRepository = kimlikRepository;
        this.studentRepository = studentRepository;
        this.vorkerRepository = vorkerRepository;
        this.activityLogRepository = activityLogRepository;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/")
    public String showMainPage(Model model) {

        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            model.addAttribute("clientCount", 0);
            model.addAttribute("studentCount", 0);
            model.addAttribute("workerCount", 0);
            model.addAttribute("recentActivities", java.util.List.of());

            return "Kimlik/homePage";
        }

        Long companyId = currentUser.getCompany().getId();

        model.addAttribute("clientCount", kimlikRepository.countByCompanyId(companyId));
        model.addAttribute("studentCount", studentRepository.countByCompanyId(companyId));
        model.addAttribute("workerCount", vorkerRepository.countByCompanyId(companyId));

        model.addAttribute("recentActivities",
                activityLogRepository.findTop5ByCompanyIdOrderByCreatedAtDesc(companyId));

        return "Kimlik/homePage";
    }
}