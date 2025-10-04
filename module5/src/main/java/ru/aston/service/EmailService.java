package ru.aston.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendUserCreated(String email, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Приветствие.");
        message.setText("Здравствуйте, " + userName + "! Ваш аккаунт был успешно создан!");
        mailSender.send(message);
    }

    public void sendUserDeleted(String email, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Удаление аккаунта.");
        message.setText("Здравствуйте, " + userName + "! Ваш аккаунт был успешно удалён!");
        mailSender.send(message);
    }
}
