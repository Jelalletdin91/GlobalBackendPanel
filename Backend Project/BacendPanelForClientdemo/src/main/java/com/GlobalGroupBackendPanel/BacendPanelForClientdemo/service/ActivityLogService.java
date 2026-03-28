package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;

import java.util.List;

public interface ActivityLogService {

    void save(String actionType, String entityType, String entityName, String description);
    List<ActivityLog> findTop5();

    List<ActivityLog> findAll();
}
