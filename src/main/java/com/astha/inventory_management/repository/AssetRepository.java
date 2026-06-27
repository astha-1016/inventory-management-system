package com.astha.inventory_management.repository;

import com.astha.inventory_management.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    @Query("""
            SELECT a FROM Asset a
            WHERE LOWER(a.assetName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.assetType) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Asset> searchAssets(String keyword);
}