import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User findById(int id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            return session.find(User.class, id);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void save(User user) {
        Transaction transaction;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        }
    }

    @Override
    public void delete(int id) {
        Transaction transaction;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        }
    }
}
