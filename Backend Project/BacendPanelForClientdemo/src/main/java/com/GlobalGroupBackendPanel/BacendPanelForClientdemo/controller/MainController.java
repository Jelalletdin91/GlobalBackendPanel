package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.ActivityLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final VorkerRepository vorkerRepository;
    private final ActivityLogService activityLogService;

    public MainController(KimlikRepository kimlikRepository,
                          StudentRepository studentRepository,
                          VorkerRepository vorkerRepository,
                          ActivityLogService activityLogService) {

        this.kimlikRepository = kimlikRepository;
        this.studentRepository = studentRepository;
        this.vorkerRepository = vorkerRepository;
        this.activityLogService = activityLogService;
    }

    @GetMapping("/")
    public String showMainPage(Model model){

        // 🔢 Counts
        long clientCount = kimlikRepository.count();
        long studentCount = studentRepository.count();
        long workerCount = vorkerRepository.count();

        model.addAttribute("clientCount", clientCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("workerCount", workerCount);

        // 🔥 Recent Activity (последние 5 действий)
        model.addAttribute("recentActivities", activityLogService.findTop5());

        return "Kimlik/homePage";
    }
}