package co.zoltans.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
@OpenAPIDefinition(
        info = @Info(title = "ToDo List API", version = "1.0.0"),
        servers = {@Server(url = "http://localhost:8080")},
        tags = {@Tag(name = "TodoItems", description = "This is a demo API for the ToDo List application.")}
)
*/
public class TodoListApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoListApplication.class, args);
    }

}
