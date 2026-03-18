package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;

import java.util.List;

public interface StudentService {

    List<StudentKimlik> findAll();

    StudentKimlik findById(Long id);

    StudentKimlik save(StudentKimlik studentKimlik);

    void deleteById(Long id);

}
