package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Role;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.RoleRepository;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyContextService companyContextService;

    public EmployeeServiceImpl(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               CompanyContextService companyContextService) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.companyContextService = companyContextService;
    }

    @Override
    public List<AppUser> findAllEmployee() {
        Long companyId = companyContextService.getCurrentCompanyId();
        return userRepository.findByCompanyId(companyId);
    }

    @Override
    public AppUser findById(Long id) {
        Long companyId = companyContextService.getCurrentCompanyId();

        AppUser employee = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getCompany() == null ||
                !employee.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return employee;
    }

    @Override
    public void save(AppUser employee, boolean kimlikRole, boolean studentRole, boolean workerRole) {

        Long companyId = companyContextService.getCurrentCompanyId();

        var existingEmail = userRepository.findByEmail(employee.getEmail());

        if (existingEmail.isPresent()) {
            if (employee.getId() == null || !existingEmail.get().getId().equals(employee.getId())) {
                throw new RuntimeException("This email already exists. Please choose another email.");
            }
        }

        var existingUsername = userRepository.findByUsername(employee.getUsername());

        if (existingUsername.isPresent()) {
            if (employee.getId() == null || !existingUsername.get().getId().equals(employee.getId())) {
                throw new RuntimeException("This username already exists. Please choose another username.");
            }
        }

        AppUser dbEmployee = null;

        if (employee.getId() != null) {
            dbEmployee = findById(employee.getId());
        }

        if (dbEmployee != null) {
            boolean employeeIsYonetici = dbEmployee.getRoles().stream()
                    .anyMatch(r -> r.getName().equals("YONETICI"));

            if (employeeIsYonetici) {
                throw new RuntimeException("YONETICI cannot be updated here");
            }

            dbEmployee.setName(employee.getName());
            dbEmployee.setUsername(employee.getUsername());
            dbEmployee.setEmail(employee.getEmail());
            dbEmployee.setEnabled(employee.isEnabled());

            if (employee.getPassword() != null && !employee.getPassword().trim().isEmpty()) {
                dbEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }

            dbEmployee.setRoles(buildRoles(kimlikRole, studentRole, workerRole));
            dbEmployee.setCompany(companyContextService.getCurrentCompany());

            userRepository.save(dbEmployee);

        } else {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setEnabled(true);
            employee.setRoles(buildRoles(kimlikRole, studentRole, workerRole));
            employee.setCompany(companyContextService.getCurrentCompany());

            userRepository.save(employee);
        }
    }

    private Set<Role> buildRoles(boolean kimlikRole, boolean studentRole, boolean workerRole) {
        Set<Role> roles = new HashSet<>();

        if (kimlikRole) {
            roles.add(roleRepository.findByName("KIMLIK")
                    .orElseThrow(() -> new RuntimeException("KIMLIK role not found")));
        }

        if (studentRole) {
            roles.add(roleRepository.findByName("STUDENT_KIMLIK")
                    .orElseThrow(() -> new RuntimeException("STUDENT_KIMLIK role not found")));
        }

        if (workerRole) {
            roles.add(roleRepository.findByName("VORKER_KIMLIK")
                    .orElseThrow(() -> new RuntimeException("VORKER_KIMLIK role not found")));
        }

        if (roles.isEmpty()) {
            throw new RuntimeException("At least one role must be selected");
        }

        return roles;
    }

    @Override
    public void deleteById(Long id) {
        AppUser employee = findById(id);

        boolean employeeIsYonetici = employee.getRoles().stream()
                .anyMatch(r -> r.getName().equals("YONETICI"));

        if (employeeIsYonetici) {
            throw new RuntimeException("YONETICI cannot be deleted");
        }

        userRepository.deleteById(id);
    }
}