package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails Nurmuhammet = User.builder()
                .username("Nurmuhammet")
                .password("{noop}Rejepov96!")
                .roles("Yonetici")
                .build();

        UserDetails Kimlik = User.builder()
                .username("Kimlik")
                .password("{noop}Kimlik96!")
                .roles("Kimlik")
                .build();

        UserDetails StudentKimlik = User.builder()
                .username("StudentKimlik")
                .password("{noop}StudentKimlik96!")
                .roles("StudentKimlik")
                .build();

        UserDetails VorkerKimlik = User.builder()
                .username("VorkerKimlik")
                .password("{noop}VorkerKimlik96!")
                .roles("VorkerKimlik")
                .build();

        return new InMemoryUserDetailsManager(Nurmuhammet, Kimlik, StudentKimlik, VorkerKimlik);
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(configurer->
                configurer
                        .anyRequest().authenticated())
                .formLogin(form->
                        form
                                .loginPage("loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                );
        return httpSecurity.build();
    }

}
