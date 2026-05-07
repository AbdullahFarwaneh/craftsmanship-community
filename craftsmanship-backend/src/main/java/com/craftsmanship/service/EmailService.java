package com.craftsmanship.service;

import com.craftsmanship.dto.RequestCreateDTO;
import com.craftsmanship.entity.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class EmailService {
    private final JavaMailSender mail;

    public void notifyCraftsman(Service service, RequestCreateDTO dto) {
        try {
            String to = service.getCraftsman().getEmail();
            if (to == null || to.isBlank()) return;
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("New Request – " + service.getName());
            msg.setText(
                "Hello " + service.getCraftsman().getFullName() + ",\n\n" +
                "New request for: " + service.getName() + "\n" +
                "Customer: " + dto.getCustomerName() + "\n" +
                "Phone: " + dto.getPhoneNumber() + "\n" +
                "Address: " + dto.getAddress() + "\n\n" +
                "– Craftsmanship Community"
            );
            mail.send(msg);
        } catch (Exception e) {
            log.warn("Email failed: {}", e.getMessage());
        }
    }
}
