package com.astha.inventory_management.repository;

import com.astha.inventory_management.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Long> {

    @Query("SELECT p FROM Part p WHERE p.quantity <= p.minimumStock")
    List<Part> findLowStockParts();

    @Query("""
            SELECT p FROM Part p
            WHERE LOWER(p.partName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.supplier) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Part> searchParts(String keyword);
}