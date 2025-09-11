package ru.aston.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.entity.User;
import ru.aston.exceptions.EntityNotFoundException;
import ru.aston.models.UpsertUserRequest;
import ru.aston.models.UserResponse;
import ru.aston.repository.UserRepository;

import java.text.MessageFormat;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(UpsertUserRequest upsertUserRequest) {
        User user = new User();
        user.setName(upsertUserRequest.getName());
        user.setEmail(upsertUserRequest.getEmail());
        user.setAge(upsertUserRequest.getAge());
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", id)));
        return convertToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public UserResponse updateUser(Integer id, UpsertUserRequest upsertUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", id)));
        user.setName(upsertUserRequest.getName());
        user.setEmail(upsertUserRequest.getEmail());
        user.setAge(upsertUserRequest.getAge());
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(MessageFormat.format("Пользователь с ID {0} не найден", id));
        }
        userRepository.deleteById(id);
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
