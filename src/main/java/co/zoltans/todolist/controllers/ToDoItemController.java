package co.zoltans.todolist.controllers;

import co.zoltans.todolist.entities.ToDoItem;
import co.zoltans.todolist.services.ToDoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/todoItems")
public class ToDoItemController {

    private final ToDoItemService toDoItemService;

    @GetMapping
    @Operation(
            tags = {"TodoItems"},
            summary = "Get all todo items",
            description = "Get all todo items",
            operationId = "getAllToDoItems",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public List<ToDoItem> getMementos() {
        return toDoItemService.getToDoItems();
    }

    @GetMapping(path = "{toDoItemId}")
    public ToDoItem getMemento(@PathVariable("toDoItemId") Long id) {
        return toDoItemService.getToDoItem(id);
    }

    @PostMapping(path = "username/{username}")
    public void createNewMemento(@PathVariable("username") String username, @RequestBody ToDoItem toDoItem) {
        toDoItemService.createToDoItem(username, toDoItem);
    }

    @PutMapping(path = "{toDoItemId}")
    public void updateMemento(@PathVariable("toDoItemId") Long id, @RequestBody ToDoItem toDoItem) {
        toDoItemService.updateToDoItem(id, toDoItem);
    }

    @DeleteMapping(path = "{toDoItemId}")
    public void deleteMemento(@PathVariable("toDoItemId") Long id) {
        toDoItemService.deleteToDoItem(id);
    }
}
