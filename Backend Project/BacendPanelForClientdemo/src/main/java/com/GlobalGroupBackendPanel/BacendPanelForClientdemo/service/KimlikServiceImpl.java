package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KimlikServiceImpl implements KimlikerService {

    private final KimlikRepository kimlikRepository;
    private final ActivityLogService activityLogService;
    private final CompanyContextService companyContextService;

    public KimlikServiceImpl(KimlikRepository kimlikRepository,
                             ActivityLogService activityLogService,
                             CompanyContextService companyContextService) {
        this.kimlikRepository = kimlikRepository;
        this.activityLogService = activityLogService;
        this.companyContextService = companyContextService;
    }

    @Override
    public List<Kimlik> findAll() {
        Long companyId = companyContextService.getCurrentCompanyId();
        return kimlikRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
    }

    @Override
    public List<Kimlik> search(String keyword) {
        Long companyId = companyContextService.getCurrentCompanyId();

        if (keyword == null || keyword.trim().isEmpty()) {
            return kimlikRepository.findByCompanyIdOrderByKimlikEndDateAsc(companyId);
        }

        return kimlikRepository.searchByCompanyIdAndKeyword(companyId, keyword);
    }

    @Override
    public Kimlik findById(Long theId) {
        Long companyId = companyContextService.getCurrentCompanyId();

        Kimlik kimlik = kimlikRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Kimlik not found"));

        if (kimlik.getCompany() == null ||
                !kimlik.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return kimlik;
    }

    @Override
    public Kimlik save(Kimlik kimlik) {
        Company company = companyContextService.getCurrentCompany();

        boolean isNew = (kimlik.getId() == null);

        if (isNew) {
            kimlik.setCompany(company);
            kimlik.setNotified60Days(false);
        } else {
            Kimlik existing = findById(kimlik.getId());
            kimlik.setCompany(existing.getCompany());
        }

        Kimlik savedKimlik = kimlikRepository.save(kimlik);

        String fullName = savedKimlik.getFirstName() + " " + savedKimlik.getLastName();

        if (isNew) {
            activityLogService.save("CREATE", "KIMLIK", fullName, "New client added: " + fullName);
        } else {
            activityLogService.save("UPDATE", "KIMLIK", fullName, "Client updated: " + fullName);
        }

        return savedKimlik;
    }

    @Override
    public void deleteById(Long theId) {
        Kimlik kimlik = findById(theId);

        String fullName = kimlik.getFirstName() + " " + kimlik.getLastName();

        kimlikRepository.deleteById(theId);

        activityLogService.save("DELETE", "KIMLIK", fullName, "Client Deleted: " + fullName);
    }
}