package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.Security;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AuthenticationConfig {

    public AuthenticationConfig(AuthenticationManagerBuilder auth,
                                InMemoryUserDetailsManager inMemoryUserDetailsManager,
                                CustomUserDetailsService customUserDetailsService,
                                PasswordEncoder passwordEncoder) throws Exception {

        auth.userDetailsService(inMemoryUserDetailsManager)
                .passwordEncoder(passwordEncoder);

        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}