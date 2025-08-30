import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Виктор", "smk666@rambler.ru", 36);
        testUser.setId(1);
    }

    @Test
    void testGetUserById() {
        when(userDao.findById(1)).thenReturn(testUser);
        User foundUser = userService.getUserById(1);
        assertEquals("Виктор", foundUser.getName());
        verify(userDao).findById(1);
    }

    @Test
    void testGetAllUsers() {
        User user = new User("Светлана", "daisy@mail.ru", 37);
        user.setId(2);
        when(userDao.findAll()).thenReturn(Arrays.asList(testUser, user));
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userDao).findAll();
    }

    @Test
    void testCreateUser() {
        User createdUser = userService.createUser("Виктор", "smk666@rambler.ru", 36);
        assertEquals("Виктор", createdUser.getName());
        verify(userDao).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("Виктор", savedUser.getName());
    }

    @Test
    void testUpdateUser() {
        when(userDao.findById(1)).thenReturn(testUser);
        User updatedUser = userService.updateUser(1, "Виктор", "smk666@mail.ru", 37);
        assertEquals("Виктор", updatedUser.getName());
        assertEquals("smk666@mail.ru", updatedUser.getEmail());
        assertEquals(37, updatedUser.getAge());
        verify(userDao).update(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals("Виктор", capturedUser.getName());
        assertEquals("smk666@mail.ru", capturedUser.getEmail());
        assertEquals(37, capturedUser.getAge());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1);
        verify(userDao).delete(1);
    }
}
