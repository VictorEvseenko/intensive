package ru.aston.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.aston.event.UserEvent;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserEventProducer {
    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendUserCreatedEvent(String email, String userName) {
        UserEvent event = new UserEvent("USER_CREATED", email, userName, LocalDateTime.now());
        kafkaTemplate.send(TOPIC, email, event);
    }

    public void sendUserDeletedEvent(String email, String userName) {
        UserEvent event = new UserEvent("USER_DELETED", email, userName, LocalDateTime.now());
        kafkaTemplate.send(TOPIC, email, event);
    }
}
