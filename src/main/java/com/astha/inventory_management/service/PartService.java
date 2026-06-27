package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Part;
import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.repository.PartRepository;
import com.astha.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    public Part savePart(Part part) {
        Part existingPart = null;
        if (part.getId() != null) {
            existingPart = partRepository.findById(part.getId()).orElse(null);
        }

        Part savedPart = partRepository.save(part);

        checkAndSendLowStockAlert(existingPart, savedPart);

        return savedPart;
    }

    public Part getPartById(Long id) {
        return partRepository.findById(id).orElse(null);
    }

    public void deletePart(Long id) {
        partRepository.deleteById(id);
    }

    public List<Part> getLowStockParts() {
        return partRepository.findLowStockParts();
    }

    private void checkAndSendLowStockAlert(Part oldPart, Part newPart) {

        boolean isLowNow = newPart.getQuantity() < newPart.getMinimumStock();

        // Case 1: brand new part being added and it is already low
        if (oldPart == null && isLowNow) {
            sendAlertToManagers(newPart);
            return;
        }

        // Case 2: existing part updated
        if (oldPart != null) {
            boolean wasLowBefore = oldPart.getQuantity() < oldPart.getMinimumStock();

            // Send only when it crosses from safe -> low
            if (!wasLowBefore && isLowNow) {
                sendAlertToManagers(newPart);
            }
        }
    }

    private void sendAlertToManagers(Part part) {
        List<User> managers = userRepository.findByRole(Role.MANAGER);

        if (!managers.isEmpty()) {
            mailService.sendLowStockAlert(part, managers);
        }
    }

    public List<Part> searchParts(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return partRepository.findAll();
        }

        return partRepository.searchParts(keyword);
    }
}