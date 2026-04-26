package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.KimlikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class KimlikServiceImpl implements KimlikerService {

    private final KimlikRepository kimlikRepository;
    private final ActivityLogService activityLogService;
    private final CurrentUserService currentUserService;

    @Autowired
    public KimlikServiceImpl(KimlikRepository kimlikRepository,
                             ActivityLogService activityLogService,
                             CurrentUserService currentUserService) {
        this.kimlikRepository = kimlikRepository;
        this.activityLogService = activityLogService;
        this.currentUserService = currentUserService;
    }

    // ✅ ВСЕ ДАННЫЕ ТОЛЬКО СВОЕЙ КОМПАНИИ
    @Override
    public List<Kimlik> findAll() {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        return kimlikRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                currentUser.getCompany().getId()
        );
    }

    // ✅ SEARCH С ИЗОЛЯЦИЕЙ
    @Override
    public List<Kimlik> search(String keyword) {
        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return kimlikRepository.findByCompanyIdOrderByKimlikEndDateAsc(
                    currentUser.getCompany().getId()
            );
        }

        return kimlikRepository.searchByCompanyIdAndKeyword(
                currentUser.getCompany().getId(),
                keyword
        );
    }

    // ✅ ЗАЩИТА ОТ ЧУЖИХ ID
    @Override
    public Kimlik findById(Long theId) {
        AppUser currentUser = currentUserService.getCurrentUser();

        Kimlik kimlik = kimlikRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Kimlik not found"));

        if (kimlik.getCompany() == null ||
                currentUser.getCompany() == null ||
                !kimlik.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return kimlik;
    }

    // ✅ SAVE С ПРИВЯЗКОЙ К КОМПАНИИ
    @Override
    public Kimlik save(Kimlik kimlik) {

        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        boolean isNew = (kimlik.getId() == null);

        if (isNew) {
            kimlik.setCompany(currentUser.getCompany());
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

    // ✅ DELETE С ПРОВЕРКОЙ
    @Override
    public void deleteById(Long theId) {

        Kimlik kimlik = findById(theId); // 🔥 уже проверяет компанию

        String fullName = kimlik.getFirstName() + " " + kimlik.getLastName();

        kimlikRepository.deleteById(theId);

        activityLogService.save("DELETE", "KIMLIK", fullName, "Client Deleted: " + fullName);
    }
}