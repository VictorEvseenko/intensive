package ru.aston.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.service.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/created")
    public String sendUserCreated(@RequestParam String email, @RequestParam String userName) {
        emailService.sendUserCreated(email, userName);
        return "Письмо о создании отправлено: " + email;
    }

    @PostMapping("/deleted")
    public String sendUserDeleted(@RequestParam String email, @RequestParam String userName) {
        emailService.sendUserDeleted(email, userName);
        return "Письмо об удалении отправлено : " + email;
    }
}
