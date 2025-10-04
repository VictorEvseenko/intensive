package ru.aston;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import ru.aston.event.UserEvent;
import ru.aston.kafka.UserEventConsumer;
import ru.aston.service.EmailService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
public class NotificationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.8.0"));

    @Autowired
    private UserEventConsumer userEventConsumer;

    @MockitoBean
    private EmailService emailService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void TestSendUserCreated() {
        UserEvent event = new UserEvent();
        event.setEventType("USER_CREATED");
        event.setEmail("test@example.ru");
        event.setUserName("testUser");

        userEventConsumer.consumeUserEvent(event);

        verify(emailService, times(1)).sendUserCreated("test@example.ru", "testUser");
    }

    @Test
    void TestSendUserDeleted() {
        UserEvent event = new UserEvent();
        event.setEventType("USER_DELETED");
        event.setEmail("test@example.ru");
        event.setUserName("testUser");

        userEventConsumer.consumeUserEvent(event);

        verify(emailService, times(1)).sendUserDeleted("test@example.ru", "testUser");
    }
}
