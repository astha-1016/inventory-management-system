package com.astha.inventory_management.config;

import com.astha.inventory_management.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()

                        // View pages (Manager + Viewer)
                        .requestMatchers(
                                "/",
                                "/dashboard",
                                "/assets",
                                "/parts",
                                "/maintenance",
                                "/users"
                        ).hasAnyRole("MANAGER", "VIEWER")

                        // Report Download (Manager + Viewer)
                        .requestMatchers("/report/export")
                        .hasAnyRole("MANAGER", "VIEWER")

                        // Manager Only
                        .requestMatchers(
                                "/assets/new",
                                "/assets/save",
                                "/assets/edit/**",
                                "/assets/update/**",
                                "/assets/delete/**",

                                "/parts/new",
                                "/parts/save",
                                "/parts/edit/**",
                                "/parts/update/**",
                                "/parts/delete/**",

                                "/maintenance/new",
                                "/maintenance/save",
                                "/maintenance/edit/**",
                                "/maintenance/update/**",
                                "/maintenance/delete/**",

                                "/users/new",
                                "/users/delete/**"
                        ).hasRole("MANAGER")

                        // Authenticated users can edit/save their own profile
                        .requestMatchers(
                                "/users/edit/**",
                                "/users/save"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}