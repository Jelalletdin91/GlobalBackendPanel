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
    private final CurrentUserService currentUserService;

    public EmployeeServiceImpl(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               CurrentUserService currentUserService) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<AppUser> findAllEmployee() {
        AppUser currentUser = currentUserService.getCurrentUser();

        // Developer sees all users
        boolean isDeveloper = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("DEVELOPER"));

        if (isDeveloper) {
            return userRepository.findAll();
        }

        // Yonetici sees only users from own company
        if (currentUser.getCompany() == null) {
            throw new RuntimeException("Current user has no company");
        }

        return userRepository.findByCompanyId(currentUser.getCompany().getId());
    }

    @Override
    public AppUser findById(Long id) {
        AppUser currentUser = currentUserService.getCurrentUser();

        AppUser employee = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean isDeveloper = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("DEVELOPER"));

        if (isDeveloper) {
            return employee;
        }

        if (currentUser.getCompany() == null ||
                employee.getCompany() == null ||
                !employee.getCompany().getId().equals(currentUser.getCompany().getId())) {
            throw new RuntimeException("ACCESS DENIED");
        }

        return employee;
    }

    @Override
    public void save(AppUser employee, boolean kimlikRole, boolean studentRole, boolean workerRole) {

        AppUser currentUser = currentUserService.getCurrentUser();

        boolean isDeveloper = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("DEVELOPER"));

        var existingEmail = userRepository.findByEmail(employee.getEmail());

        if (existingEmail.isPresent()) {
            if (employee.getId() == null || !existingEmail.get().getId().equals(employee.getId())) {
                throw new RuntimeException("This email already exists. Please choose another email.");
            }
        }

        AppUser dbEmployee = null;

        if (employee.getId() != null) {
            dbEmployee = findById(employee.getId());
        }

        if (dbEmployee != null) {
            dbEmployee.setName(employee.getName());
            dbEmployee.setUsername(employee.getUsername());
            dbEmployee.setEmail(employee.getEmail());
            dbEmployee.setEnabled(employee.isEnabled());

            if (employee.getPassword() != null && !employee.getPassword().trim().isEmpty()) {
                dbEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }

            Set<Role> roles = buildRoles(kimlikRole, studentRole, workerRole);
            dbEmployee.setRoles(roles);

            // Developer can preserve existing company; Yonetici cannot move company
            if (!isDeveloper) {
                dbEmployee.setCompany(currentUser.getCompany());
            }

            userRepository.save(dbEmployee);

        } else {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setEnabled(true);

            Set<Role> roles = buildRoles(kimlikRole, studentRole, workerRole);
            employee.setRoles(roles);

            // 🔥 auto company assignment
            if (!isDeveloper) {
                if (currentUser.getCompany() == null) {
                    throw new RuntimeException("Current user has no company");
                }
                employee.setCompany(currentUser.getCompany());
            }

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
        AppUser currentUser = currentUserService.getCurrentUser();
        AppUser employee = findById(id);

        boolean employeeIsYonetici = employee.getRoles().stream().anyMatch(r -> r.getName().equals("YONETICI"));

        if (employeeIsYonetici) {
            throw new RuntimeException("YONETICI cannot be deleted");
        }

        if (currentUser.getId().equals(employee.getId())) {
            throw new RuntimeException("You cannot delete yourself");
        }

        userRepository.deleteById(id);
    }
}