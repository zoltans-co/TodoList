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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ToDoItemTest {

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
    void createNewMemento() {
        // given
        final var user = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        user.addMemento(new ToDoItem(user, "Buy milk", Priority.HIGH));
        user.addMemento(new ToDoItem(user, "Buy eggs", Priority.LOW));
        user.addMemento(new ToDoItem(user, "Buy cheese", Priority.MEDIUM));

        // when
        userService.createUser(user);

        final var resultUser = userService.getUserByUserName("JohnDoe");
        final var mementosUser = resultUser.getToDoItems();

        // then
        assertNotNull(resultUser, "User should not be null");
        assertEquals(3, mementosUser.size(), "There should be 3 mementosUser1");
        assertEquals("Buy milk", mementosUser.get(0).getReminder(), "Reminder should be 'Buy milk'");
        assertEquals("Buy eggs", mementosUser.get(1).getReminder(), "Reminder should be 'Buy eggs'");
        assertEquals("Buy cheese", mementosUser.get(2).getReminder(), "Reminder should be 'Buy cheese'");
    }

    @Test
    void getUserMementos() {
        // given
        final var initialUser1 = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        initialUser1.addMemento(new ToDoItem(initialUser1, "Buy milk", Priority.HIGH));
        initialUser1.addMemento(new ToDoItem(initialUser1, "Buy eggs", Priority.LOW));
        userRepository.save(initialUser1);

        final var initialUser2 = new User("Jane", "Doe", "JaneDoe", "jane.doe@gmail.com", "secret");
        initialUser2.addMemento(new ToDoItem(initialUser2, "Buy cheese", Priority.HIGH));
        initialUser2.addMemento(new ToDoItem(initialUser2, "Buy bread", Priority.LOW));
        initialUser2.addMemento(new ToDoItem(initialUser2, "Buy pizza", Priority.LOW));
        userRepository.save(initialUser2);

        // when
        final var resultUser1 = userService.getUserByUserName("JohnDoe");
        final var mementosUser1 = resultUser1.getToDoItems();

        final var resultUser2 = userService.getUserByUserName("JaneDoe");
        final var mementosUser2 = resultUser2.getToDoItems();

        // then
        assertNotNull(resultUser1, "User should not be null");
        assertEquals(2, mementosUser1.size(), "There should be 2 mementosUser1");
        assertEquals("Buy milk", mementosUser1.get(0).getReminder(), "Reminder should be 'Buy milk'");
        assertEquals("Buy eggs", mementosUser1.get(1).getReminder(), "Reminder should be 'Buy eggs'");

        assertNotNull(resultUser2, "User should not be null");
        assertEquals(3, mementosUser2.size(), "There should be 3 mementosUser2");
        assertEquals("Buy cheese", mementosUser2.get(0).getReminder(), "Reminder should be 'Buy cheese'");
        assertEquals("Buy bread", mementosUser2.get(1).getReminder(), "Reminder should be 'Buy bread'");
        assertEquals("Buy pizza", mementosUser2.get(2).getReminder(), "Reminder should be 'Buy pizza'");
    }

    @Test
    void updateMemento() {
        // given
        final var user = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        userRepository.save(user);
        var memento = new ToDoItem(user, "Buy milk", Priority.MEDIUM);
        toDoItemRepository.save(memento);

        // when
        memento.setReminder("Buy cheese instead of milk");
        toDoItemService.updateToDoItem(memento.getId(), memento);

        // then
        assertEquals("Buy cheese instead of milk", toDoItemRepository.findById(memento.getId()).get().getReminder());
    }

    @Test
    void deleteMemento() {
        // given
        final var user = new User("John", "Doe", "JohnDoe", "j.doe@gmail.com", "secret");
        userRepository.save(user);
        var memento = new ToDoItem(user, "Buy milk", Priority.MEDIUM);
        toDoItemRepository.save(memento);

        // when
        toDoItemService.deleteToDoItem(memento.getId());

        // then
        assertEquals(0, toDoItemRepository.count());
    }

}