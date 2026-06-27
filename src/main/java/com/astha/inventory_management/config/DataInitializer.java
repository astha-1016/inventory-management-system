package com.astha.inventory_management.config;

import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("manager").isEmpty()) {
                User manager = new User();
                manager.setUsername("manager");
                manager.setPassword(passwordEncoder.encode("manager123"));
                manager.setRole(Role.MANAGER);
                userRepository.save(manager);
            }

            if (userRepository.findByUsername("viewer").isEmpty()) {
                User viewer = new User();
                viewer.setUsername("viewer");
                viewer.setPassword(passwordEncoder.encode("viewer123"));
                viewer.setRole(Role.VIEWER);
                userRepository.save(viewer);
            }
        };
    }
}