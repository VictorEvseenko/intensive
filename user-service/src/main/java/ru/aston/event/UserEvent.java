package ru.aston.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserEvent {
    private String eventType;
    private String email;
    private String userName;
    private LocalDateTime timestamp;
}
