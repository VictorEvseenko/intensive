package ru.aston.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import ru.aston.controller.UserController;
import ru.aston.entity.User;
import ru.aston.models.UserResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserResponse> {

    public UserAssembler() {
        super(UserController.class, UserResponse.class);
    }

    @Override
    public UserResponse toModel(User user) {
        UserResponse response = convertToResponse(user);
        addLinks(response, user.getId());
        return response;
    }

    public UserResponse toModel(UserResponse userResponse) {
        addLinks(userResponse, userResponse.getId());
        return userResponse;
    }

    public void addLinks(UserResponse response, Integer userId) {
        response.add(linkTo(methodOn(UserController.class)
                .getUserById(userId)).withSelfRel());

        response.add(linkTo(methodOn(UserController.class)
                .getAllUsers()).withRel("users"));

        response.add(linkTo(methodOn(UserController.class)
                .updateUser(userId, null)).withRel("update"));

        response.add(linkTo(methodOn(UserController.class)
                .deleteUser(userId)).withRel("delete"));
    }

    private UserResponse convertToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt());
    }
}
