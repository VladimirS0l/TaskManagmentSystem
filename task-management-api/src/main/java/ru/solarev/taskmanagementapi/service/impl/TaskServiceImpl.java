package ru.solarev.taskmanagementapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.exceptions.ResourceAccessDeniedException;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;
import ru.solarev.taskmanagementapi.repository.TaskRepository;
import ru.solarev.taskmanagementapi.repository.specifications.TaskSpecifications;
import ru.solarev.taskmanagementapi.service.TaskService;
import ru.solarev.taskmanagementapi.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public Page<Task> findAllTasksAuthor(String email, Priority priority,
                                            Integer page, Integer pageSize) {
        Specification<Task> spec = Specification.where(null);
        User user = userService.findByEmail(email);
        if (user != null) {
            spec = spec.and(TaskSpecifications.findByAuthor(user));
        }
        if (priority != null) {
            spec = spec.and(TaskSpecifications.findByPriority(priority));
        }
        return taskRepository
                .findAll(spec, PageRequest.of(page - 1, pageSize));
    }

    @Override
    public Page<Task> findAllTasksByUserId(Long id, Integer page, Integer pageSize) {
        Specification<Task> spec = Specification.where(null);
        User user = userService.findById(id);
        if (user != null) {
            spec = spec.and(TaskSpecifications.findByAuthor(user));
        }
        return taskRepository
                .findAll(spec, PageRequest.of(page - 1, pageSize));
    }

    @Override
    public Task findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Задача не найдена"));
    }

    @Override
    public Task create(Task task, String email) {
        var author = userService.findByEmail(email);
        var assignee = userService.findByName(task.getAssignee());
        task.setId(null);
        task.setAuthor(author);
        task.setAssignee(assignee.getName());
        task.setComments(new ArrayList<>());

        return taskRepository.save(task);
    }

    @Override
    public Task update(Task updateTask, String email) {
        checkAuthor(email, updateTask);
        var task = findById(updateTask.getId());
        task.setTitle(updateTask.getTitle());
        task.setDescription(updateTask.getDescription());

        task.setUpdatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id, String email) {
        var task = findById(id);
        checkAuthor(email, task);
        taskRepository.delete(task);
    }

    @Override
    public Task updateStatus(Status status, Long taskId,
                             Long assigneeId, String email) {
        Task task = findById(taskId);
        var author = userService.findByEmail(email);
        var assignee = userService.findById(assigneeId);
        if (author == task.getAuthor() ||
                assignee.getName().equals(task.getAssignee())) {
            task.setStatus(status);
            task.setUpdatedDate(LocalDateTime.now());
        }
        return taskRepository.save(task);
    }

    @Override
    public Task setAssignee(Long assigneeId, Long taskId, String email) {
        var user = userService.findById(assigneeId);
        var task = findById(taskId);
        checkAuthor(email, task);
        task.setAssignee(user.getName());
        task.setUpdatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }


    private void checkAuthor(String authorEmail, Task task) {
        var author = userService.findByEmail(authorEmail);
        if (author != task.getAuthor()) {
            throw new ResourceAccessDeniedException("Вы не можете изменить чужую задачу");
        }
    }
}
