package ru.solarev.taskmanagementapi.service;

import org.springframework.data.domain.Page;
import ru.solarev.taskmanagementapi.dto.TaskDto;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.util.List;

public interface TaskService {
    Page<Task> findAllTasksAuthor(String email, Priority priority,
                                     Integer page, Integer pageSize);
    Page<Task> findAllTasksByUserId(Long id, Integer page, Integer pageSize);
    Task findById(Long taskId);
    Task create(Task task, String email);
    Task update(Task updateTask, String email);
    void delete(Long id, String email);
    Task updateStatus(Status status, Long taskId, Long assigneeId, String email);
    Task setAssignee(Long assigneeId, Long taskId, String email);
}
