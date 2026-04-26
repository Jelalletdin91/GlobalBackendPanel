package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.Security;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public DemoSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Only developer stays in memory
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails developer = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("DEVELOPER")
                .build();

        return new InMemoryUserDetailsManager(developer);
    }

    @Bean
    public AuthenticationProvider inMemoryAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(inMemoryUserDetailsManager());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider databaseAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager() {
        return new ProviderManager(
                inMemoryAuthenticationProvider(),
                databaseAuthenticationProvider()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests(configurer ->
                        configurer
                                // public pages
                                .requestMatchers(
                                        "/loginPage",
                                        "/authenticateTheUser",
                                        "/company/register",
                                        "/css/**",
                                        "/js/**",
                                        "/images/**"
                                ).permitAll()

                                // developer only
                                .requestMatchers("/developer/**").hasRole("DEVELOPER")

                                // company pages
                                .requestMatchers("/company/**").hasAnyRole("YONETICI", "DEVELOPER")

                                // employee management
                                .requestMatchers("/employee/**").hasAnyRole("YONETICI", "DEVELOPER")

                                // modules
                                .requestMatchers("/kimlik/**").hasAnyRole("KIMLIK", "YONETICI", "DEVELOPER")
                                .requestMatchers("/Student_kimlik/**").hasAnyRole("STUDENT_KIMLIK", "YONETICI", "DEVELOPER")
                                .requestMatchers("/VorkerKimlik/**").hasAnyRole("VORKER_KIMLIK", "YONETICI", "DEVELOPER")

                                // home page
                                .requestMatchers("/").hasAnyRole(
                                        "DEVELOPER",
                                        "YONETICI",
                                        "KIMLIK",
                                        "STUDENT_KIMLIK",
                                        "VORKER_KIMLIK"
                                )

                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/loginPage")
                                .permitAll()
                );

        return http.build();
    }
}