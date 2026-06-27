package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Part;
import com.astha.inventory_management.entity.Role;
import com.astha.inventory_management.entity.User;
import com.astha.inventory_management.repository.PartRepository;
import com.astha.inventory_management.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PartService {

    private final PartRepository partRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    // ✅ All dependencies via constructor — no @Autowired
    public PartService(PartRepository partRepository,
                       UserRepository userRepository,
                       MailService mailService) {
        this.partRepository = partRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    // ✅ Efficient count — no need to load all records
    public long countParts() {
        return partRepository.count();
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

    public List<Part> searchParts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return partRepository.findAll();
        }
        return partRepository.searchParts(keyword);
    }

    private void checkAndSendLowStockAlert(Part oldPart, Part newPart) {
        boolean isLowNow = newPart.getQuantity() < newPart.getMinimumStock();

        // Case 1: brand new part already low
        if (oldPart == null && isLowNow) {
            sendAlertToManagers(newPart);
            return;
        }

        // Case 2: existing part crossed from safe → low
        if (oldPart != null) {
            boolean wasLowBefore = oldPart.getQuantity() < oldPart.getMinimumStock();
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
}