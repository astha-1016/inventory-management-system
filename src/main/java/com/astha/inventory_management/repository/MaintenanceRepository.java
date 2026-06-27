package com.astha.inventory_management.repository;

import com.astha.inventory_management.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    @Query("""
            SELECT m FROM Maintenance m
            WHERE LOWER(m.asset.assetName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(m.technician) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(m.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Maintenance> searchMaintenance(String keyword);

}