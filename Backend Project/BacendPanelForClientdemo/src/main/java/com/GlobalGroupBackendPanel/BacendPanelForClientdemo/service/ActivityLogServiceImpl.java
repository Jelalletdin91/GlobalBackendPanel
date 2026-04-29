package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final CompanyContextService companyContextService;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository,
                                  CompanyContextService companyContextService) {
        this.activityLogRepository = activityLogRepository;
        this.companyContextService = companyContextService;
    }

    @Override
    public void save(String actionType, String entityType, String entityName, String description) {
        AppUser currentUser = companyContextService.getCurrentUserOrNull();
        Company company = companyContextService.getCurrentCompany();

        ActivityLog log = new ActivityLog(actionType, entityType, entityName, description);

        log.setUser(currentUser);
        log.setCompany(company);

        activityLogRepository.save(log);
    }

    @Override
    public List<ActivityLog> findTop5() {
        Long companyId = companyContextService.getCurrentCompanyId();

        return activityLogRepository.findTop5ByCompanyIdOrderByCreatedAtDesc(companyId);
    }

    @Override
    public List<ActivityLog> findAll() {
        Long companyId = companyContextService.getCurrentCompanyId();

        return activityLogRepository.findByCompanyIdOrderByCreatedAtDesc(companyId);
    }
}