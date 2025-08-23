import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.*;

public class AppInterface {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDaoImpl();
    private static final Logger logger = LogManager.getLogger(AppInterface.class);

    public static void main(String[] args) {
        logger.log(Level.INFO, "Старт работы приложения.");
        boolean runningApp = true;
        try {
            while (runningApp) {
                System.out.println("Выберите действие:");
                System.out.println("1.Показать всех пользователей");
                System.out.println("2.Показать пользователя по ID");
                System.out.println("3.Добавить пользователя");
                System.out.println("4.Обновить пользователя по ID");
                System.out.println("5.Удалить пользователя по ID");
                System.out.println("6.Завершить работу приложения");

                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        getAllUsers();
                        break;
                    case "2":
                        getUserById();
                        break;
                    case "3":
                        createUser();
                        break;
                    case "4":
                        updateUser();
                        break;
                    case "5":
                        deleteUser();
                        break;
                    case "6":
                        runningApp = false;
                        break;
                    default:
                        System.out.println("Некорректный ввод!");
                }
            }
        } catch (RuntimeException ex) {
            logger.log(Level.ERROR, ex.getMessage());
            System.out.println(ex.getMessage());
        } finally {
            HibernateConfiguration.closeSessionFactory();
            logger.log(Level.INFO, "Завершение работы приложения.");
        }
    }

    private static void getAllUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Список пользователей пуст!");
        } else {
            System.out.println("Список пользователей:");
            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    private static void getUserById() {
        System.out.println("Введите ID пользователя: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userDao.findById(id);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("Пользователь с таким ID не найден!");
        }
    }

    private static void createUser() {
        System.out.println("Введите имя нового пользователя: ");
        String name = scanner.nextLine();

        System.out.println("Введите email: ");
        String email = scanner.nextLine();

        System.out.println("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        userDao.save(user);
        System.out.println("Пользователь успешно создан!");
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для изменения: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь с таким ID не найден!");
            return;
        }

        System.out.println(user);
        System.out.print("Введите новое имя пользователя: ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Введите новый email: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Введите возраст: ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        userDao.update(user);
        System.out.println("Пользователь успешно обновлен!");
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());

        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь с таким ID не найден!");
            return;
        }

        userDao.delete(id);
        System.out.println("Пользователь успешно удален!");
    }
}
