package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KimlikRepository  extends JpaRepository<Kimlik, Long> {

    List<Kimlik> findAllByOrderByKimlikEndDateAsc();



}
