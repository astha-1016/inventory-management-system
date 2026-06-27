package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Asset;
import com.example.inventorymanagement.repository.AssetRepository;
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