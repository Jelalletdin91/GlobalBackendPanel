package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.VorkerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VorkerKimlikServiceImpl implements VorkerKimlikService {

    private final VorkerRepository vorkerRepository;
    private final ActivityLogService activityLogService;
    private final CompanyContextService companyContextService;

    public VorkerKimlikServiceImpl(VorkerRepository vorkerRepository,
                                   ActivityLogService activityLogService,
                                   CompanyContextService companyContextService) {
        this.vorkerRepository = vorkerRepository;
        this.activityLogService = activityLogService;
        this.companyContextService = companyContextService;
    }

    @Override
    public List<VorkerKimlik> findAll() {
        Long companyId = companyContextService.getCurrentCompanyId();
        return vorkerRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
    }

    @Override
    public List<VorkerKimlik> search(String keyword) {
        Long companyId = companyContextService.getCurrentCompanyId();

        if (keyword == null || keyword.trim().isEmpty()) {
            return vorkerRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
        }

        return vorkerRepository.searchByCompanyIdAndKeyword(companyId, keyword);
    }

    @Override
    public VorkerKimlik findById(Long theId) {
        Long companyId = companyContextService.getCurrentCompanyId();

        VorkerKimlik vorkerKimlik = vorkerRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        if (vorkerKimlik.getCompany() == null ||
                !vorkerKimlik.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return vorkerKimlik;
    }

    @Override
    public VorkerKimlik save(VorkerKimlik vorkerKimlik) {
        Company company = companyContextService.getCurrentCompany();

        boolean isNew = (vorkerKimlik.getId() == null);

        if (isNew) {
            vorkerKimlik.setCompany(company);
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