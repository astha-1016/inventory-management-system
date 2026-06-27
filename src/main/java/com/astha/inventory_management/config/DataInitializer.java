package com.astha.inventory_management.config;

import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // ✅ Read from environment / application.properties — not hardcoded
    @Value("${app.default.manager.password:manager123}")
    private String managerPassword;

    @Value("${app.default.viewer.password:viewer123}")
    private String viewerPassword;

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("manager").isEmpty()) {
                User manager = new User();
                manager.setUsername("manager");
                manager.setPassword(passwordEncoder.encode(managerPassword));
                manager.setRole(Role.MANAGER);
                userRepository.save(manager);
            }

            if (userRepository.findByUsername("viewer").isEmpty()) {
                User viewer = new User();
                viewer.setUsername("viewer");
                viewer.setPassword(passwordEncoder.encode(viewerPassword));
                viewer.setRole(Role.VIEWER);
                userRepository.save(viewer);
            }
        };
    }
}