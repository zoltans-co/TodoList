package co.zoltans.todolist;

import co.zoltans.todolist.entities.ToDoItem;
import co.zoltans.todolist.repositories.ToDoItemRepository;
import co.zoltans.todolist.services.ToDoItemService;
import co.zoltans.todolist.enums.Priority;
import co.zoltans.todolist.entities.User;
import co.zoltans.todolist.repositories.UserRepository;
import co.zoltans.todolist.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest {

    private UserService userService;
    private ToDoItemService toDoItemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        toDoItemService = new ToDoItemService(userRepository, toDoItemRepository);

        // start with a clean slate
        toDoItemRepository.deleteAll();
        toDoItemRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @AfterEach
    void tearDown() {
        // clean up after each test
        toDoItemRepository.deleteAll();
        toDoItemRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    void createUser() {
        // given
        final var user = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        user.addMemento(new ToDoItem(user, "Buy milk", Priority.HIGH));
        user.addMemento(new ToDoItem(user, "Buy eggs", Priority.LOW));
        user.addMemento(new ToDoItem(user, "Buy cheese", Priority.MEDIUM));


        // when
        userService.createUser(user);

        // then
        assertEquals(1, userRepository.count());
        assertEquals(3, toDoItemRepository.count());
    }

    @Test
    void deleteUser() {
        // given
        final var user = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        user.addMemento(new ToDoItem(user, "Buy milk", Priority.HIGH));
        user.addMemento(new ToDoItem(user, "Buy eggs", Priority.LOW));
        user.addMemento(new ToDoItem(user, "Buy cheese", Priority.MEDIUM));
        userService.createUser(user);

        // when
        userService.deleteUser(user.getId());

        // then
        assertEquals(0, userRepository.count());
        assertEquals(0, toDoItemRepository.count());
    }

}