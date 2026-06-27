package com.astha.inventory_management.controller;

import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // Save user
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

    // Edit user
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);

        if (user == null) {
            return "redirect:/users";
        }

        // Get logged-in user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));

        // If not manager, allow edit only for own profile
        if (!isManager && !user.getUsername().equals(loggedInUsername)) {
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("pageTitle", "Edit User");
        return "user-form";
    }
}