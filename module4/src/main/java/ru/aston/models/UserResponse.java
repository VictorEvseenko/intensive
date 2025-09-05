package ru.aston.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}
