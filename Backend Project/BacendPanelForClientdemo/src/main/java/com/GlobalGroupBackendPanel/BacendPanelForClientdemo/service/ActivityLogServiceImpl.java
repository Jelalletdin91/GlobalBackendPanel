package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService{

    private ActivityLogRepository activityLogRepository;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository){
        this.activityLogRepository=activityLogRepository;
    }


    @Override
    public void save(String actionType, String entityType, String entityName, String description) {
        ActivityLog log = new ActivityLog(actionType, entityName, entityType, description);
        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLog> findTop5() {
        return activityLogRepository.findTop5ByOrderByCreatedAtdesc();
    }
}
