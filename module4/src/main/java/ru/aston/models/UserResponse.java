package ru.aston.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(collectionRelation = "users", itemRelation = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse extends RepresentationModel<UserResponse> {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}
