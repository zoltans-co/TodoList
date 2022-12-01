package co.zoltans.todolist.services;

import co.zoltans.todolist.entities.ToDoItem;
import co.zoltans.todolist.exceptions.NotFoundException;
import co.zoltans.todolist.repositories.ToDoItemRepository;
import co.zoltans.todolist.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ToDoItemService {

    private final UserRepository userRepository;
    private final ToDoItemRepository toDoItemRepository;

    public List<ToDoItem> getToDoItems() {
        log.info("getToDoItems was called");
        return toDoItemRepository.findAll();
    }

    public ToDoItem getToDoItem(Long id) {
        log.info("getToDoItem was called with id: {}", id);
        var mementoResult = toDoItemRepository.findById(id);
        log.info("getToDoItem result: {}", mementoResult);
        return mementoResult.orElseThrow(() -> new NotFoundException("Memento not found with id: " + id));
    }

    public ToDoItem getMementoByReminder(String reminder) {
        return toDoItemRepository
            .findByReminder(reminder)
            .orElseThrow(() -> new NotFoundException("ToDoItem not found with reminder: " + reminder));
    }

    public void createToDoItem(final String userName, final ToDoItem toDoItem) {
        log.info("createToDoItem was called");
        userRepository.findByUserName(userName).ifPresent(user -> {
            toDoItem.setUser(user);
            toDoItemRepository.save(toDoItem);
        });
    }

    public void updateToDoItem(Long id, ToDoItem toDoItem) {
        log.info("updateToDoItem was called");
        // find the memento and update it
        ToDoItem toDoItemToUpdate = toDoItemRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("ToDoItem not found with id: " + id));
        toDoItemToUpdate.setReminder(toDoItem.getReminder());
        toDoItemRepository.saveAndFlush(toDoItem);
    }

    public void deleteToDoItem(Long id) {
        log.info("deleteToDoItem was called");
        toDoItemRepository.deleteById(id);
        toDoItemRepository.flush();
    }
}
