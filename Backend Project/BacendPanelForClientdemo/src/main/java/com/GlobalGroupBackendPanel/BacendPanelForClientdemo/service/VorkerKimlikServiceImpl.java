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
    private final ActivityLogService activityLogService;

    @Autowired
    public VorkerKimlikServiceImpl (VorkerRepository vorkerRepository, ActivityLogService activityLogService){
        this.vorkerRepository=vorkerRepository;
        this.activityLogService=activityLogService;
    }

    @Override
    public List<VorkerKimlik> findAll() {
        return vorkerRepository.findAllByOrderByKimlikEndDateAsc();
    }

    @Override
    public List<VorkerKimlik> search(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()){
            return vorkerRepository.findAllByOrderByKimlikEndDateAsc();
        }else{
            return vorkerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrKimlikNumberContainingIgnoreCaseOrPhoneNumberContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByKimlikEndDateAsc(keyword, keyword, keyword, keyword, keyword);
        }
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

        boolean isNew = (vorkerKimlik.getId() == null);

        if (isNew) {
            vorkerKimlik.setNotified60Days(false);
        }

        VorkerKimlik savedVorkerKimlik = vorkerRepository.save(vorkerKimlik);
        String fullName = savedVorkerKimlik.getFirstName() + " " + savedVorkerKimlik.getLastName();

        if (isNew) {
            activityLogService.save("CREATE", "WORKER", fullName, "New worker added: " + fullName);
        } else {
            activityLogService.save("UPDATE", "WORKER", fullName, "Worker updated: " + fullName);
        }

        return savedVorkerKimlik;
    }

    @Override
    public void deleteById(Long theId) {

        VorkerKimlik vorkerKimlik = findById(theId);
        String fullName = vorkerKimlik.getFirstName()+ " " + vorkerKimlik.getLastName();
        vorkerRepository.deleteById(theId);

        activityLogService.save("DELETE", "WORKER", fullName, "Worker Deleted: " + fullName);
    }
}
