package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.repository.UserRepository;
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
                return;
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return;
        }

        User existingUser = userRepository.findById(user.getId()).orElse(null);

        if (existingUser != null) {
            // Always keep old password — password change is handled separately via changePassword()
            user.setPassword(existingUser.getPassword());

            // Only MANAGER can change role
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

            if (!isManager) {
                user.setRole(existingUser.getRole());
            }
        }

        userRepository.save(user);
    }

    // ✅ Secure password change — verifies current password before updating
    public String changePassword(Long userId, String currentPassword,
                                 String newPassword, String confirmPassword) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "User not found.";
        }

        // Step 1 — verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect.";
        }

        // Step 2 — check new and confirm match
        if (!newPassword.equals(confirmPassword)) {
            return "New password and confirm password do not match.";
        }

        // Step 3 — check new password is not same as old
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return "New password must be different from current password.";
        }

        // Step 4 — check minimum length
        if (newPassword.length() < 6) {
            return "New password must be at least 6 characters.";
        }

        // Step 5 — encode and save
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "success";
    }

    // ✅ Secure email/username update — verifies current password before updating
    public String updateProfile(Long userId, String newUsername,
                                String newEmail, String currentPassword) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "User not found.";
        }

        // Verify current password before allowing profile changes
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Password is incorrect. Cannot update profile.";
        }

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        userRepository.save(user);

        return "success";
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}