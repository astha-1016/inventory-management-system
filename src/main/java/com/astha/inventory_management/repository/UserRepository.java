package com.astha.inventory_management.repository;

import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(CAST(u.role AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<User> searchUsers(String keyword);
}