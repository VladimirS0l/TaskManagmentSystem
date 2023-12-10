package ru.solarev.taskmanagementapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.solarev.taskmanagementapi.dto.TaskDto;
import ru.solarev.taskmanagementapi.dto.mapper.TaskMapper;
import ru.solarev.taskmanagementapi.dto.validation.OnCreate;
import ru.solarev.taskmanagementapi.dto.validation.OnUpdate;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;
import ru.solarev.taskmanagementapi.service.TaskService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Задачи", description = "Методы для управления задачами")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("showTasksAuthor")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на получение всех задач пользователя")
    public Page<TaskDto> showAuthorTasks(Principal principal,
                @Parameter(description = "Приоритет задачи")
                @RequestParam(name = "priority", required = false) Priority priority,
                @Parameter(description = "Номер текущей страницы")
                @RequestParam(name = "p", defaultValue = "1") Integer page,
                @Parameter(description = "Общее количество страниц")
                @RequestParam(name = "page_size", defaultValue = "9") Integer pageSize) {
        return taskService
                .findAllTasksAuthor(principal.getName(), priority, page, pageSize)
                .map(taskMapper::toDto);
    }

    @GetMapping("show/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на получение всех задач пользователя по ID")
    public Page<TaskDto> showAllTasksUserById(
            @Parameter(description = "Id пользователя")
            @PathVariable("id") Long id,
            @Parameter(description = "Номер текущей страницы")
                @RequestParam(name = "p", defaultValue = "1") Integer page,
            @Parameter(description = "Общее количество страниц")
                @RequestParam(name = "page_size", defaultValue = "9") Integer pageSize) {
        return taskService
                .findAllTasksByUserId(id, page, pageSize)
                .map(taskMapper::toDto);
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание новой задачи")
    public TaskDto createTask(@Validated(OnCreate.class) @RequestBody TaskDto taskDto, Principal principal) {
        return taskMapper.toDto(taskService.create(
                taskMapper.toEntity(taskDto), principal.getName()));
    }

    @PutMapping("update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить задачу")
    public TaskDto updateTask(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto, Principal principal) {
        return taskMapper.toDto(taskService.update(
                taskMapper.toEntity(taskDto), principal.getName()));
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удалить задачу по ID")
    public void deleteTask(@Parameter(description = "Id задачи")
            @PathVariable("id") Long id, Principal principal) {
        taskService.delete(id, principal.getName());
    }

    @GetMapping("update/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить статус задачи")
    public TaskDto updateStatus(
            @Parameter(description = "Id задачи")
            @RequestParam(name = "task_id", required = false) Long taskId,
            @Parameter(description = "Статус задачи")
            @RequestParam(name = "status", required = false) Status status,
            @Parameter(description = "Id исполнителя")
            @RequestParam(name = "assignee_id", required = false) Long assigneeId,
            Principal principal
    ) {
        return taskMapper.toDto(taskService
                .updateStatus(status, taskId, assigneeId, principal.getName()));
    }

    @GetMapping("update/assignee")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменить исполнителя задачи")
    public TaskDto updateAssignee(
            @Parameter(description = "Id пользователя")
            @RequestParam(name = "user_id", required = false) Long userId,
            @Parameter(description = "Id задачи")
            @RequestParam(name = "task_id", required = false) Long taskId,
            Principal principal
    ) {
        return taskMapper.toDto(taskService
                .setAssignee(userId, taskId, principal.getName()));
    }

}
