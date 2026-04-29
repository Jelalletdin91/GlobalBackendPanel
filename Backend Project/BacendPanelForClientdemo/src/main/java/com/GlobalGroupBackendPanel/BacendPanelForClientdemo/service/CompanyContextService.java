package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.CompanyRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CompanyContextService {

    private final CurrentUserService currentUserService;
    private final CompanyRepository companyRepository;
    private final HttpSession session;

    public CompanyContextService(CurrentUserService currentUserService,
                                 CompanyRepository companyRepository,
                                 HttpSession session) {
        this.currentUserService = currentUserService;
        this.companyRepository = companyRepository;
        this.session = session;
    }

    public boolean isDeveloper() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DEVELOPER"));
    }

    public AppUser getCurrentUserOrNull() {
        if (isDeveloper()) {
            return null;
        }

        return currentUserService.getCurrentUser();
    }

    public Long getCurrentCompanyId() {
        if (isDeveloper()) {
            Object companyId = session.getAttribute("DEV_COMPANY_ID");

            if (companyId == null) {
                throw new RuntimeException("Developer did not select company");
            }

            if (companyId instanceof Long) {
                return (Long) companyId;
            }

            if (companyId instanceof Integer) {
                return ((Integer) companyId).longValue();
            }

            if (companyId instanceof Number) {
                return ((Number) companyId).longValue();
            }

            return Long.valueOf(companyId.toString());
        }

        AppUser currentUser = currentUserService.getCurrentUser();

        if (currentUser.getCompany() == null) {
            throw new RuntimeException("User has no company");
        }

        return currentUser.getCompany().getId();
    }

    public Company getCurrentCompany() {
        Long companyId = getCurrentCompanyId();

        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
}