package com.astha.inventory_management.controller;

import com.astha.inventory_management.service.AssetService;
import com.astha.inventory_management.service.MaintenanceService;
import com.astha.inventory_management.service.PartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AssetService assetService;
    private final PartService partService;
    private final MaintenanceService maintenanceService;

    public DashboardController(AssetService assetService,
                               PartService partService,
                               MaintenanceService maintenanceService) {
        this.assetService = assetService;
        this.partService = partService;
        this.maintenanceService = maintenanceService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {

        // ✅ Use count queries instead of loading all records just to get size
        model.addAttribute("totalAssets", assetService.countAssets());
        model.addAttribute("totalParts", partService.countParts());
        model.addAttribute("totalMaintenance", maintenanceService.countMaintenance());

        // Low stock count still needs the list (to show details too)
        model.addAttribute("lowStockPartsCount", partService.getLowStockParts().size());

        // These are needed to display the actual lists on dashboard
        model.addAttribute("assets", assetService.getAllAssets());
        model.addAttribute("lowStockParts", partService.getLowStockParts());
        model.addAttribute("maintenanceRecords", maintenanceService.getAllMaintenanceRecords());

        return "dashboard";
    }
}