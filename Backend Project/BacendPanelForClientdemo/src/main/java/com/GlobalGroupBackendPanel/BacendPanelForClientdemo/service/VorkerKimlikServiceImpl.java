package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VorkerKimlikServiceImpl implements VorkerKimlikService {

    private final VorkerRepository vorkerRepository;
    private final ActivityLogService activityLogService;
    private final CurrentUserService currentUserService;

    @Autowired
    public VorkerKimlikServiceImpl(VorkerRepository vorkerRepository,
                                   ActivityLogService activityLogService,
                                   CurrentUserService currentUserService) {
        this.vorkerRepository = vorkerRepository;
        this.activityLogService = activityLogService;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<VorkerKimlik> findAll() {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        return vorkerRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                currentUser.getCompany().getId()
        );
    }

    @Override
    public List<VorkerKimlik> search(String keyword) {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return vorkerRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                    currentUser.getCompany().getId()
            );
        }

        return vorkerRepository.searchByCompanyIdAndKeyword(
                currentUser.getCompany().getId(),
                keyword
        );
    }

    @Override
    public VorkerKimlik findById(Long theId) {
        AppUser currentUser = currentUserService.getCurrentUser();

        VorkerKimlik vorkerKimlik = vorkerRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (vorkerKimlik.getCompany() == null ||
                currentUser.getCompany() == null ||
                !vorkerKimlik.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return vorkerKimlik;
    }

    @Override
    public VorkerKimlik save(VorkerKimlik vorkerKimlik) {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        boolean isNew = (vorkerKimlik.getId() == null);

        if (isNew) {
            vorkerKimlik.setCompany(currentUser.getCompany());
            vorkerKimlik.setNotified60Days(false);
        } else {
            VorkerKimlik existing = findById(vorkerKimlik.getId());
            vorkerKimlik.setCompany(existing.getCompany());
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

        String fullName = vorkerKimlik.getFirstName() + " " + vorkerKimlik.getLastName();

        vorkerRepository.deleteById(theId);

        activityLogService.save("DELETE", "WORKER", fullName, "Worker Deleted: " + fullName);
    }
}