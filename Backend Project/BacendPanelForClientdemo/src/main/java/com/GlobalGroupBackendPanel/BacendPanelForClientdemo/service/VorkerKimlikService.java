package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;

import java.util.List;

public interface VorkerKimlikService {

    List<VorkerKimlik> findAll();

    List<VorkerKimlik> search(String keyword);

    VorkerKimlik findById(Long theId);

    VorkerKimlik save (VorkerKimlik vorkerKimlik);

    void deleteById(Long theId);

}
