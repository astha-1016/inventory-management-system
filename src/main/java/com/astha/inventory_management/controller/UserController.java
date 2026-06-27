package com.astha.inventory_management.controller;

import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List all users
    @GetMapping
    public String listUsers(
            @RequestParam(required = false) String keyword,
            Model model) {
        model.addAttribute("users", userService.searchUsers(keyword));
        model.addAttribute("keyword", keyword);
        return "users";
    }

    // Show add user form
    @GetMapping("/new")
    public String showUserForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager) {
            return "redirect:/users";
        }

        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("pageTitle", "Add New User");
        return "user-form";
    }

    // Save user (create or update basic info — NOT password)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager) {
            return "redirect:/users";
        }

        userService.deleteUser(id);
        return "redirect:/users";
    }

    // Edit user — show form
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);

        if (user == null) {
            return "redirect:/users";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager && !user.getUsername().equals(loggedInUsername)) {
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("pageTitle", "Edit User");
        return "user-form";
    }

    // ✅ Show change password form
    @GetMapping("/change-password/{id}")
    public String showChangePasswordForm(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User user = userService.getUserById(id);
        if (user == null) return "redirect:/users";

        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        // Only allow changing own password (managers can change anyone's)
        if (!isManager && !user.getUsername().equals(loggedInUsername)) {
            return "redirect:/users";
        }

        model.addAttribute("userId", id);
        return "change-password";
    }

    // ✅ Handle change password form submission
    @PostMapping("/change-password/{id}")
    public String changePassword(
            @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        String result = userService.changePassword(id, currentPassword, newPassword, confirmPassword);

        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
            return "redirect:/users";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
            return "redirect:/users/change-password/" + id;
        }
    }

    // ✅ Show update profile form (email/username)
    @GetMapping("/update-profile/{id}")
    public String showUpdateProfileForm(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User user = userService.getUserById(id);
        if (user == null) return "redirect:/users";

        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        if (!isManager && !user.getUsername().equals(loggedInUsername)) {
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        return "update-profile";
    }

    // ✅ Handle update profile form submission
    @PostMapping("/update-profile/{id}")
    public String updateProfile(
            @PathVariable Long id,
            @RequestParam String newUsername,
            @RequestParam String newEmail,
            @RequestParam String currentPassword,
            RedirectAttributes redirectAttributes) {

        String result = userService.updateProfile(id, newUsername, newEmail, currentPassword);

        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/users";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
            return "redirect:/users/update-profile/" + id;
        }
    }
}