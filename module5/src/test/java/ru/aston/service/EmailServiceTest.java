package ru.aston.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender javaMailSender;

    @Test
    void TestSendUserCreated() {
        emailService.sendUserCreated("test@example.ru", "testUser");
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void TestSendUserDeleted() {
        emailService.sendUserDeleted("test@example.ru", "testUser");
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

}
