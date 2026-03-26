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
    private final ActivityLogService activityLogService;

    @Autowired
    public KimlikServiceImpl(KimlikRepository theKimlikRepository, ActivityLogService activityLogService){
        kimlikRepository=theKimlikRepository;
        this.activityLogService=activityLogService;
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

        if(kimlik.getId() == null){
            kimlik.setNotified60Days(false);
        }

        Kimlik savedKimlik = kimlikRepository.save(kimlik);

        String fullName = savedKimlik.getFirstName() + " " + savedKimlik.getLastName();

        if (kimlik.getId() == null){
            activityLogService.save("CREATE", "CLIENT", fullName, "new client added: "+ fullName);
        }else {
            activityLogService.save("UDATE", "CLIENT", fullName, "Client Updated: " + fullName);
        }

        return savedKimlik;
    }

    @Override
    public void deleteById(Long theId) {

        kimlikRepository.deleteById(theId);
    }



}
