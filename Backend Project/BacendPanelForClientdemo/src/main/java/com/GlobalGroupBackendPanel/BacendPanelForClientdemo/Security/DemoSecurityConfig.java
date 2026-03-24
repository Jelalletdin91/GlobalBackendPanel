package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails nurmuhammet = User.builder()
                .username("Nurmuhammet")
                .password(passwordEncoder().encode("Rejepov96!"))
                .roles("Kimlik", "StudentKimlik", "VorkerKimlik", "EMPLOYEE")
                .build();

        UserDetails kimlik = User.builder()
                .username("Kimlik")
                .password(passwordEncoder().encode("Kimlik96"))
                .roles("Kimlik", "EMPLOYEE")
                .build();

        UserDetails studentKimlik = User.builder()
                .username("StudentKimlik")
                .password(passwordEncoder().encode("StudentKimlik96!"))
                .roles("StudentKimlik", "EMPLOYEE")
                .build();

        UserDetails vorkerKimlik = User.builder()
                .username("VorkerKimlik")
                .password(passwordEncoder().encode("VorkerKimlik96!"))
                .roles("VorkerKimlik", "EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(
                nurmuhammet,
                kimlik,
                studentKimlik,
                vorkerKimlik
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/loginPage", "/authenticateTheUser").permitAll()
                        .requestMatchers("/").hasRole("EMPLOYEE")

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
}