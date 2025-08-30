import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb").withUsername("test").withPassword("test");

    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl();
        clearDatabase();
    }

    @AfterAll
    static void afterAll() {
        HibernateConfiguration.closeSessionFactory();
    }

    private void clearDatabase() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User("Виктор", "smk666@rambler.ru", 36);
        userDao.save(user);
        User foundUser = userDao.findById(user.getId());
        assertEquals("Виктор", foundUser.getName());
        assertEquals("smk666@rambler.ru", foundUser.getEmail());
        assertEquals(36, foundUser.getAge());
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User("Виктор", "smk666@rambler.ru", 36);
        User user2 = new User("Светлана", "svetl@rambler.ru", 37);
        userDao.save(user1);
        userDao.save(user2);
        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        User user = new User("Виктор", "smk666@rambler.ru", 36);
        userDao.save(user);
        user.setEmail("smk666@mail.ru");
        user.setAge(37);
        userDao.update(user);
        User updatedUser = userDao.findById(user.getId());
        assertEquals("Виктор", updatedUser.getName());
        assertEquals("smk666@mail.ru", updatedUser.getEmail());
        assertEquals(37, updatedUser.getAge());
    }

    @Test
    void testDeleteUser() {
        User user = new User("Виктор", "smk666@rambler.ru", 36);
        userDao.save(user);
        userDao.delete(user.getId());
        User deletedUser = userDao.findById(user.getId());
        assertNull(deletedUser);
    }
}
