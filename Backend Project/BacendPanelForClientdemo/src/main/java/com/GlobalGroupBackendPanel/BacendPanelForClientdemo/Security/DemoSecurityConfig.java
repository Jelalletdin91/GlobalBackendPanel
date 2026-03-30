package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.Security;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DemoSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public DemoSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Только Yonetici в памяти
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails yonetici = User.builder()
                .username("Nurmuhammet")
                .password(passwordEncoder().encode("Rejepov96!"))
                .roles("Yonetici", "EMPLOYEE", "Kimlik", "StudentKimlik", "VorkerKimlik")
                .build();

        return new InMemoryUserDetailsManager(yonetici);
    }

    @Bean
    public AuthenticationProvider databaseAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/loginPage", "/authenticateTheUser").permitAll()

                        .requestMatchers("/").hasRole("EMPLOYEE")

                        .requestMatchers("/employee/**").hasRole("Yonetici")

                        .requestMatchers("/kimlik/**").hasRole("Kimlik")
                        .requestMatchers("/Student_kimlik/**").hasRole("StudentKimlik")
                        .requestMatchers("/VorkerKimlik/**").hasRole("VorkerKimlik")

                        .anyRequest().authenticated()
        );

        http.formLogin(form ->
                form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
        );

        http.logout(logout ->
                logout
                        .logoutSuccessUrl("/loginPage")
                        .permitAll()
        );

        return http.build();
    }

    @Bean
    public org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authenticationConfiguration() {
        return new org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration();
    }
}