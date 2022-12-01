package co.zoltans.todolist.entities;

import co.zoltans.todolist.enums.Priority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@ToString
@Setter
@NoArgsConstructor
@Table(name = "todo_item")
@Entity(name = "ToDoItem")
public class ToDoItem {

    @Id
    @SequenceGenerator(
            name = "todo_item_sequence",
            sequenceName = "todo_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "todo_item_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(name = "reminder")
    private String reminder;

    @Column(name = "priority")
    private Priority priority;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_todo_item_fk"
            )
    )
    private User user;

    public ToDoItem(final User user, final String reminder, final Priority priority) {
        this.user = user;
        this.reminder = reminder;
        this.priority = priority;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("reminder")
    public String getReminder() {
        return reminder;
    }

    @JsonProperty("priority")
    public Priority getPriority() {
        return priority;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public User getUser() {
        return user;
    }
}
