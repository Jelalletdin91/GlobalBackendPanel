package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import org.springframework.stereotype.Service;

import java.util.List;


public interface EmployeeService {

    List<AppUser> findAllEmployee();

    AppUser findById(Long id);

    void save(AppUser employee, boolean kimlikRole, boolean studentRole, boolean workerRole);

    void deleteById(Long id);



}
