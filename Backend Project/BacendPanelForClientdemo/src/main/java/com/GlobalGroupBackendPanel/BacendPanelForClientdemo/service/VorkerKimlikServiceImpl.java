package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VorkerKimlikServiceImpl implements VorkerKimlikService{


    private VorkerRepository vorkerRepository;

    @Autowired
    public VorkerKimlikServiceImpl (VorkerRepository vorkerRepository){
        this.vorkerRepository=vorkerRepository;
    }

    @Override
    public List<VorkerKimlik> findAll() {
        return vorkerRepository.findAllByOrderByKimlikEndDateAsc();
    }

    @Override
    public VorkerKimlik findById(Long theId) {

        Optional<VorkerKimlik> result = vorkerRepository.findById(theId);

        VorkerKimlik vorkerKimlik = null;

        if(result.isPresent()){
            vorkerKimlik = result.get();
        }else {
            throw new RuntimeException("Did not find");
        }

        return vorkerKimlik;
    }

    @Override
    public VorkerKimlik save(VorkerKimlik vorkerKimlik) {

        if(vorkerKimlik.getId() == null){
            vorkerKimlik.setNotified60Days(false);
        }

        return vorkerRepository.save(vorkerKimlik);
    }

    @Override
    public void deleteById(Long theId) {
        vorkerRepository.deleteById(theId);
    }
}
