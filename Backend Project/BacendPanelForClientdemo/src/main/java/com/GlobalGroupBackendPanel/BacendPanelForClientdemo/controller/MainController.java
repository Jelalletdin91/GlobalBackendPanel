package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.StudentRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CompanyContextService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final KimlikRepository kimlikRepository;
    private final StudentRepository studentRepository;
    private final VorkerRepository vorkerRepository;
    private final ActivityLogRepository activityLogRepository;
    private final CompanyContextService companyContextService;

    public MainController(KimlikRepository kimlikRepository,
                          StudentRepository studentRepository,
                          VorkerRepository vorkerRepository,
                          ActivityLogRepository activityLogRepository,
                          CompanyContextService companyContextService) {
        this.kimlikRepository = kimlikRepository;
        this.studentRepository = studentRepository;
        this.vorkerRepository = vorkerRepository;
        this.activityLogRepository = activityLogRepository;
        this.companyContextService = companyContextService;
    }

    @GetMapping("/")
    public String showMainPage(Model model) {

        if (companyContextService.isDeveloper()
                && companyContextService.getSelectedDeveloperCompanyId() == null) {
            return "redirect:/developer/companies";
        }

        Long companyId = companyContextService.getCurrentCompanyId();
        Company company = companyContextService.getCurrentCompany();

        model.addAttribute("companyName", company.getName());
        model.addAttribute("companyFirstLetterOfName", company.getName().charAt(0));

        model.addAttribute("clientCount", kimlikRepository.countByCompanyId(companyId));
        model.addAttribute("studentCount", studentRepository.countByCompanyId(companyId));
        model.addAttribute("workerCount", vorkerRepository.countByCompanyId(companyId));

        model.addAttribute("recentActivities",
                activityLogRepository.findTop5ByCompanyIdOrderByCreatedAtDesc(companyId));

        return "Kimlik/homePage";
    }
}