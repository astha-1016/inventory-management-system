package com.astha.inventory_management.controller;

import com.astha.inventory_management.entity.Asset;
import com.astha.inventory_management.entity.Maintenance;
import com.astha.inventory_management.service.AssetService;
import com.astha.inventory_management.service.MaintenanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final AssetService assetService;

    public MaintenanceController(MaintenanceService maintenanceService, AssetService assetService) {
        this.maintenanceService = maintenanceService;
        this.assetService = assetService;
    }

    // List all maintenance records
    @GetMapping
    public String listMaintenance(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute(
                "maintenanceRecords",
                maintenanceService.searchMaintenance(keyword));

        model.addAttribute("keyword", keyword);

        return "maintenance";
    }

    // Show add form
    @GetMapping("/new")
    public String showMaintenanceForm(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("pageTitle", "Add Maintenance Record");
        return "maintenance-form";
    }

    // Save new maintenance
    @PostMapping("/save")
    public String saveMaintenance(@RequestParam("assetId") Long assetId,
                                  @RequestParam("issueDescription") String issueDescription,
                                  @RequestParam("maintenanceDate") String maintenanceDate,
                                  @RequestParam("technician") String technician,
                                  @RequestParam("cost") Double cost,
                                  @RequestParam("status") String status) {

        Asset asset = assetService.getAssetById(assetId);

        Maintenance maintenance = new Maintenance();
        maintenance.setAsset(asset);
        maintenance.setIssueDescription(issueDescription);
        maintenance.setMaintenanceDate(LocalDate.parse(maintenanceDate));
        maintenance.setTechnician(technician);
        maintenance.setCost(cost);
        maintenance.setStatus(status);

        maintenanceService.saveMaintenance(maintenance);
        return "redirect:/maintenance";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditMaintenanceForm(@PathVariable Long id, Model model) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id);
        model.addAttribute("maintenance", maintenance);
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("pageTitle", "Edit Maintenance Record");
        return "maintenance-form";
    }

    // Update maintenance
    @PostMapping("/update/{id}")
    public String updateMaintenance(@PathVariable Long id,
                                    @RequestParam("assetId") Long assetId,
                                    @RequestParam("issueDescription") String issueDescription,
                                    @RequestParam("maintenanceDate") String maintenanceDate,
                                    @RequestParam("technician") String technician,
                                    @RequestParam("cost") Double cost,
                                    @RequestParam("status") String status) {

        Asset asset = assetService.getAssetById(assetId);

        Maintenance maintenance = new Maintenance();
        maintenance.setId(id);
        maintenance.setAsset(asset);
        maintenance.setIssueDescription(issueDescription);
        maintenance.setMaintenanceDate(LocalDate.parse(maintenanceDate));
        maintenance.setTechnician(technician);
        maintenance.setCost(cost);
        maintenance.setStatus(status);

        maintenanceService.saveMaintenance(maintenance);
        return "redirect:/maintenance";
    }

    // Delete maintenance
    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return "redirect:/maintenance";
    }
}