package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.User;
import com.example.inventorymanagement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<User> searchUsers(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return userRepository.findAll();
        }

        return userRepository.searchUsers(keyword);
    }

    public void saveUser(User user) {

        if (user.getId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

            if (!isManager) {
                return; // non-manager cannot create users
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return;
        }

        User existingUser = userRepository.findById(user.getId()).orElse(null);

        if (existingUser != null) {

            // If password field is blank during edit, keep old password
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                // If password changed, encode new password
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            // Only MANAGER can change role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

            if (!isManager) {
                // Non-managers cannot change role
                user.setRole(existingUser.getRole());
            }
        }

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}