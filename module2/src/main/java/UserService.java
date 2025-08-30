import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.save(user);
        return user;
    }

    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User updateUser(int id, String name, String email, Integer age) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new RuntimeException("Пользователь с таким ID не найден!");
        }
        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (age != null) {
            user.setAge(age);
        }
        userDao.update(user);
        return user;
    }

    public void deleteUser(int id) {
        userDao.delete(id);
    }
}
