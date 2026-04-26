package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findAllByOrderByCreatedAtDesc();

    List<ActivityLog> findTop5ByCompanyIdOrderByCreatedAtDesc(Long companyId);

    List<ActivityLog> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
}