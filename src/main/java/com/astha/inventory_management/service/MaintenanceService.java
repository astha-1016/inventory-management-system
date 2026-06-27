package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Maintenance;
import com.astha.inventory_management.repository.MaintenanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public List<Maintenance> getAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }

    public Maintenance saveMaintenance(Maintenance maintenance) {
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id).orElse(null);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }

    public List<Maintenance> searchMaintenance(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return maintenanceRepository.findAll();
        }

        return maintenanceRepository.searchMaintenance(keyword);
    }
}