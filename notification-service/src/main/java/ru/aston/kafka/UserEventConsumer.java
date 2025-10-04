package ru.aston.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.aston.event.UserEvent;
import ru.aston.service.EmailService;

@Component
@RequiredArgsConstructor
public class UserEventConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(UserEvent event) {
        switch (event.getEventType()) {
            case "USER_CREATED":
                emailService.sendUserCreated(event.getEmail(), event.getUserName());
                break;
            case "USER_DELETED":
                emailService.sendUserDeleted(event.getEmail(), event.getUserName());
                break;
        }
    }
}
