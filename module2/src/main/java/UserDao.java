import java.util.List;

public interface UserDao {
    User findById(int id);
    List<User> findAll();
    void save(User user);
    void update(User user);
    void delete(int id);
}
