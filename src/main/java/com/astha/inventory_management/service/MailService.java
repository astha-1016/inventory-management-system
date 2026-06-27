package com.astha.inventory_management.service;

import com.astha.inventory_management.entity.Part;
import com.astha.inventory_management.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    // ✅ Constructor injection instead of @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowStockAlert(Part part, List<User> managers) {
        log.info("Low stock mail method called for part: {}", part.getPartName());

        for (User manager : managers) {
            log.info("Checking manager {} with email {}", manager.getUsername(), manager.getEmail());

            if (manager.getEmail() != null && !manager.getEmail().isBlank()) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(manager.getEmail());
                    message.setSubject("Low Stock Alert - Inventory Management");
                    message.setText(
                            "Dear Manager,\n\n" +
                                    "Low stock alert for the following part:\n\n" +
                                    "Part Name: " + part.getPartName() + "\n" +
                                    "Current Quantity: " + part.getQuantity() + "\n" +
                                    "Minimum Quantity: " + part.getMinimumStock() + "\n\n" +
                                    "Please restock it.\n\n" +
                                    "Regards,\nInventory Management System"
                    );

                    mailSender.send(message);
                    log.info("Mail sent successfully to {}", manager.getEmail());

                } catch (Exception e) {
                    log.error("Mail sending failed for {}", manager.getEmail(), e);
                }
            } else {
                log.warn("Skipping manager {} because email is empty", manager.getUsername());
            }
        }
    }
}