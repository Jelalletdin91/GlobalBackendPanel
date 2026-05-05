package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Company;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Role;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.CompanyRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.RoleRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository companyRepository,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createCompanyWithOwner(String companyName,
                                       String ownerName,
                                       String email,
                                       String username,
                                       String password,
                                       String subscriptionPlan,
                                       String invitedBy) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("This username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("This email already exists");
        }

        Company company = new Company();
        company.setName(companyName);
        company.setInvitedBy(invitedBy);

        company.setSubscriptionPlan(
                subscriptionPlan == null || subscriptionPlan.isBlank()
                        ? "MONTHLY"
                        : subscriptionPlan
        );

        company.setSubscriptionStart(LocalDate.now());

        if ("YEARLY".equalsIgnoreCase(company.getSubscriptionPlan())) {
            company.setSubscriptionEnd(LocalDate.now().plusYears(1));
        } else {
            company.setSubscriptionEnd(LocalDate.now().plusMonths(1));
        }

        company.setActive(true);

        Company savedCompany = companyRepository.save(company);

        Role yoneticiRole = roleRepository.findByName("YONETICI")
                .orElseThrow(() -> new RuntimeException("YONETICI role not found"));

        Set<Role> roles = new HashSet<>();
        roles.add(yoneticiRole);

        AppUser owner = new AppUser();
        owner.setName(ownerName);
        owner.setEmail(email);
        owner.setUsername(username);
        owner.setPassword(passwordEncoder.encode(password));
        owner.setEnabled(true);
        owner.setCompany(savedCompany);
        owner.setRoles(roles);

        userRepository.save(owner);
    }

    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setActive(false);
        companyRepository.save(company);
    }

    public void activateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setActive(true);
        companyRepository.save(company);
    }
}