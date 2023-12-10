package ru.solarev.taskmanagementapi.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.user.User;

public class TaskSpecifications {
    public static Specification<Task> findByPriority(Priority priority) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> findByAuthor(User author) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("author"), author);
    }
}
