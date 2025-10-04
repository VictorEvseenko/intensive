package ru.aston.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.entity.User;
import ru.aston.models.UpsertUserRequest;
import ru.aston.models.UserResponse;
import ru.aston.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1);
        user.setName("Виктор");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        UserResponse response = userService.getUserById(1);

        assertEquals(1, response.getId());
        assertEquals("Виктор", response.getName());
        verify(userRepository).findById(1);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setName("Виктор");
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);
        Collection<UserResponse> userResponses = userService.getAllUsers();

        assertEquals(users.size(), userResponses.size());
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUser() {
        UpsertUserRequest request = new UpsertUserRequest("Виктор", "smk666@rambler.ru", 36);
        User user = new User();
        user.setId(1);
        user.setName("Виктор");
        user.setEmail("smk666@rambler.ru");
        user.setAge(36);

        when(userRepository.save(any(User.class))).thenReturn(user);
        UserResponse response = userService.createUser(request);

        assertEquals("Виктор", response.getName());
        assertEquals("smk666@rambler.ru", response.getEmail());
        assertEquals(36, response.getAge());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("Виктор");
        existingUser.setEmail("smk666@rambler.ru");
        existingUser.setAge(36);

        UpsertUserRequest updateRequest = new UpsertUserRequest("Виктор", "smk666@yandex.ru", 37);
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Виктор");
        updatedUser.setEmail("smk666@yandex.ru");
        updatedUser.setAge(37);

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        UserResponse response = userService.updateUser(1, updateRequest);

        assertEquals("Виктор", response.getName());
        assertEquals("smk666@yandex.ru", response.getEmail());
        assertEquals(37, response.getAge());
        verify(userRepository).findById(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1)).thenReturn(true);
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }
}
