import org.apache.logging.log4j.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(AppInterface.class);

    @Override
    public User findById(int id) {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                logger.log(Level.INFO, "Пользователь с ID {} найден!", id);
            } else {
                logger.log(Level.INFO,"Пользователь с ID {} не найден!", id);
            }
            return user;
        } catch (Exception ex) {
            logger.log(Level.ERROR,"Ошибка при поиске пользователя с ID {}", id);
            throw ex;
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            logger.log(Level.INFO, "Найдено пользователей: {}", users.size());
            return users;
        } catch (Exception ex) {
            logger.log(Level.ERROR,"Ошибка при поиске всех пользователей!");
            throw ex;
        }
    }

    @Override
    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.log(Level.INFO, "Пользователь успешно сохранен!");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.ERROR,"Ошибка при сохранении пользователя!");
            throw ex;
        }
    }

    @Override
    public void update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.log(Level.INFO, "Пользователь успешно обновлен!");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.ERROR,"Ошибка при обновлении пользователя!");
            throw ex;
        }
    }

    @Override
    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateConfiguration.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.log(Level.INFO, "Пользователь с ID {} удален!", id);
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.ERROR,"Ошибка при удалении пользователя с ID {}", id);
            throw ex;
        }
    }
}
