package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final CurrentUserService currentUserService;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository,
                                  CurrentUserService currentUserService) {
        this.activityLogRepository = activityLogRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public void save(String actionType, String entityType, String entityName, String description) {
        AppUser currentUser = currentUserService.getCurrentUser();

        ActivityLog log = new ActivityLog(actionType, entityType, entityName, description);

        log.setUser(currentUser);

        if (currentUser.getCompany() != null) {
            log.setCompany(currentUser.getCompany());
        }

        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLog> findTop5() {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            return List.of();
        }

        return activityLogRepository.findTop5ByCompanyIdOrderByCreatedAtDesc(
                currentUser.getCompany().getId()
        );
    }

    @Override
    public List<ActivityLog> findAll() {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            return List.of();
        }

        return activityLogRepository.findByCompanyIdOrderByCreatedAtDesc(
                currentUser.getCompany().getId()
        );
    }
}