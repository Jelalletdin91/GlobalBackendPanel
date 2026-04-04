package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;

import java.util.List;

public interface KimlikerService {

    List<Kimlik> findAll();

    List<Kimlik> search(String keyword);

    Kimlik findById(Long theId);

    Kimlik save(Kimlik kimlik);

    void deleteById(Long theId);


}
