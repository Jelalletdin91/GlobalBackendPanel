package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class KimlikServiceImpl implements KimlikerService{

    private KimlikRepository kimlikRepository;

    @Autowired
    public KimlikServiceImpl(KimlikRepository theKimlikRepository){
        kimlikRepository=theKimlikRepository;
    }


    @Override
    public List<Kimlik> findAll() {


        return kimlikRepository.findAllByOrderByKimlikEndDateAsc();

    }

    @Override
    public Kimlik findById(Long theId) {

        Optional<Kimlik> result = kimlikRepository.findById(theId);

        Kimlik kimlik = null;

        if (result.isPresent()){
            kimlik = result.get();
        }else {
            throw new RuntimeException("Did not find!");
        }



        return kimlik;
    }

    @Override
    public Kimlik save(Kimlik kimlik) {

        if (kimlik.getKimlikEndDate() != null && kimlik.getDaysLeft() <= 60) {
            kimlik.setNotified60Days(true);
        } else {
            kimlik.setNotified60Days(false);
        }

        return kimlikRepository.save(kimlik);
    }

    @Override
    public void deleteById(Long theId) {
        kimlikRepository.deleteById(theId);
    }



}
