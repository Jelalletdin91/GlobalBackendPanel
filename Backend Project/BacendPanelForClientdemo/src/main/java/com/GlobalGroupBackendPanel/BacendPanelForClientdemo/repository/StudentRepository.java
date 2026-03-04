package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<StudentKimlik, Long> {

    List<StudentKimlik> findAllByOrderByKimlikEndDateAsc();

    List<StudentKimlik> findByNotified60DaysFalse();

    // САМЫЙ ВАЖНЫЙ метод:
    List<StudentKimlik> findByNotified60DaysFalseAndKimlikEndDateLessThanEqual(LocalDate date);
}
