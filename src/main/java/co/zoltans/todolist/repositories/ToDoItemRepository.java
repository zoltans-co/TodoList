package co.zoltans.todolist.repositories;

import co.zoltans.todolist.entities.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Long> {

    Optional<ToDoItem>  findByReminder(String reminder);

}