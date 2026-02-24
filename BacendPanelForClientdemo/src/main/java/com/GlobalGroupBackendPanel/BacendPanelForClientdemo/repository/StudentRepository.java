package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentKimlik, Long> {


}
