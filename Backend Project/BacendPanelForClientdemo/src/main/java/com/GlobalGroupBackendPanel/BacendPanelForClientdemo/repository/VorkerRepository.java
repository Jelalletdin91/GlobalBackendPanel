package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VorkerRepository extends JpaRepository<VorkerKimlik, Long> {

    List<VorkerKimlik> findAllByOrderByKimlikEndDateAsc();

    List<VorkerKimlik> findByNotified60DaysFalse();

    List<VorkerKimlik> findByNotified60DaysFalseAndKimlikEndDateLessThanEqual(LocalDate date);
}
