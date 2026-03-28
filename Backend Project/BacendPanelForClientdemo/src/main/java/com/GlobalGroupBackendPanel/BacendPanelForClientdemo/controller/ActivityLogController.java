package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.controller;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.ActivityLogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ActivityLogController {

    private ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService){
        this.activityLogService=activityLogService;
    }


    @GetMapping("/RecentActivities")
    public String recentActivities(Model model){
        List<ActivityLog> activityLogs = activityLogService.findAll();
        model.addAttribute("activityLogs", activityLogs);
        return "Kimlik/activityLog";
    }
}

