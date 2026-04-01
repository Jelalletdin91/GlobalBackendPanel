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
public class EmployeeServiceImpl implements EmployeeService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.userRepository=userRepository;
    }

    @Override
    public List<AppUser> findAllEmployee() {
        return userRepository.findAll();
    }

    @Override
    public AppUser findById(Long id) {

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public void save(AppUser employee, boolean kimlikRole, boolean studentRole, boolean workerRole) {

        AppUser dbEmployee = null;

        if (employee.getId() != null) {
            dbEmployee = findById(employee.getId());
        }

        if (dbEmployee != null) {
            dbEmployee.setName(employee.getName());
            dbEmployee.setUsername(employee.getUsername());
            dbEmployee.setEnabled(employee.isEnabled());

            if (employee.getPassword() != null && !employee.getPassword().trim().isEmpty()) {
                dbEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }

            Set<Role> roles = buildRoles(kimlikRole, studentRole, workerRole);
            dbEmployee.setRoles(roles);

            userRepository.save(dbEmployee);
        } else {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setEnabled(true);

            Set<Role> roles = buildRoles(kimlikRole, studentRole, workerRole);
            employee.setRoles(roles);

            userRepository.save(employee);
        }
    }

    private Set<Role> buildRoles(boolean kimlikRole, boolean studentRole, boolean workerRole) {
        Set<Role> roles = new HashSet<>();

        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("EMPLOYEE role not found"));
        roles.add(employeeRole);

        if (kimlikRole) {
            roles.add(roleRepository.findByName("Kimlik")
                    .orElseThrow(() -> new RuntimeException("Kimlik role not found")));
        }

        if (studentRole) {
            roles.add(roleRepository.findByName("StudentKimlik")
                    .orElseThrow(() -> new RuntimeException("StudentKimlik role not found")));
        }

        if (workerRole) {
            roles.add(roleRepository.findByName("VorkerKimlik")
                    .orElseThrow(() -> new RuntimeException("VorkerKimlik role not found")));
        }

        return roles;

    }

    @Override
    public void deleteById(Long id) {

        AppUser employee = findById(id);

        if ("Nurmuhammet".equalsIgnoreCase(employee.getUsername())){
            throw new RuntimeException("Yonetici cannot be deleted");
        }else {
            userRepository.deleteById(id);
        }

    }
}
