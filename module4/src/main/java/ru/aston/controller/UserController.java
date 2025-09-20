package ru.aston.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.assembler.UserAssembler;
import ru.aston.models.UpsertUserRequest;
import ru.aston.models.UserResponse;
import ru.aston.service.UserService;
import org.springframework.hateoas.IanaLinkRelations;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API для управления пользователями")
public class UserController {
    private final UserService userService;
    private final UserAssembler userAssembler;

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Пользователь успешно создан")})
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Данные пользователя")
            @RequestBody UpsertUserRequest upsertUserRequest) {
        UserResponse userResponse = userService.createUser(upsertUserRequest);
        UserResponse model = userAssembler.toModel(userService.getUserById(userResponse.getId()));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "ID пользователя") @PathVariable Integer id) {
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userAssembler.toModel(userService.getUserById(userResponse.getId())));
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<CollectionModel<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        CollectionModel<UserResponse> model = CollectionModel.of(users.stream()
                .map(user -> userAssembler.toModel(userService.getUserById(user.getId()))).toList());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create"));
        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя по ID", description = "Обновляет данные пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "ID пользователя") @PathVariable Integer id,
            @Parameter(description = "Обновленные данные пользователя") @RequestBody UpsertUserRequest upsertUserRequest) {
        UserResponse userResponse = userService.updateUser(id, upsertUserRequest);
        return ResponseEntity.ok(userAssembler.toModel(userService.getUserById(userResponse.getId())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID пользователя") @PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
