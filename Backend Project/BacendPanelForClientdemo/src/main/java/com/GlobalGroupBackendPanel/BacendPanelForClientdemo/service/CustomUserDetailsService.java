package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.AppUser;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Role;
import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if(appUser.getCompany() == null && appUser.getCompany().isActive()){
            throw new DisabledException("Company is disabled");
        }

        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .disabled(!appUser.isEnabled())
                .authorities(mapRolesToAuthorities(appUser))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(AppUser appUser) {
        return appUser.getRoles()
                .stream()
                .map(Role::getName)
                .map(roleName -> roleName.startsWith("ROLE_")
                        ? new SimpleGrantedAuthority(roleName)
                        : new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());
    }
}