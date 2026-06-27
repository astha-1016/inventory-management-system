package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Asset;
import com.example.inventorymanagement.service.AssetService;
import com.example.inventorymanagement.service.ExcelExportService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Show all assets
    @GetMapping
    public String listAssets(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute("assets", assetService.searchAssets(keyword));
        model.addAttribute("keyword", keyword);

        return "assets";
    }

    // Show form to add new asset
    @GetMapping("/new")
    public String showAssetForm(Model model) {
        model.addAttribute("asset", new Asset());
        model.addAttribute("pageTitle", "Add New Asset");
        return "asset-form";
    }

    // Save new asset
    @PostMapping("/save")
    public String saveAsset(@ModelAttribute Asset asset) {
        assetService.saveAsset(asset);
        return "redirect:/assets";
    }

    // Show form to edit asset
    @GetMapping("/edit/{id}")
    public String showEditAssetForm(@PathVariable Long id, Model model) {
        Asset asset = assetService.getAssetById(id);
        model.addAttribute("asset", asset);
        model.addAttribute("pageTitle", "Edit Asset");
        return "asset-form";
    }

    // Update existing asset
    @PostMapping("/update/{id}")
    public String updateAsset(@PathVariable Long id, @ModelAttribute Asset asset) {
        asset.setId(id);
        assetService.saveAsset(asset);
        return "redirect:/assets";
    }

    // Delete asset
    @GetMapping("/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return "redirect:/assets";
    }

}