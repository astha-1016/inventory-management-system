package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Asset;
import com.astha.inventory_management.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // ✅ Efficient count — doesn't load all records into memory
    public long countAssets() {
        return assetRepository.count();
    }

    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    public List<Asset> searchAssets(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return assetRepository.findAll();
        }
        return assetRepository.searchAssets(keyword);
    }
}