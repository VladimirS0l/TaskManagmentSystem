package ru.solarev.taskmanagementapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.solarev.taskmanagementapi.dto.validation.OnCreate;
import ru.solarev.taskmanagementapi.dto.validation.OnUpdate;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;

@Getter
@Setter
@Schema(description = "Модель задачи")
public class TaskDto {
    @Schema(description = "ID задачи", example = "1")
    @NotNull(message = "ID не может быть пустым", groups = OnUpdate.class)
    private Long id;
    @Schema(description = "Название задачи", example = "Сделать рефакторинг кода")
    @Size(min = 1, max = 255, message = "Название не может быть меньше 1," +
            " и больше 255 символов", groups = {OnUpdate.class, OnCreate.class})
    private String title;
    @Schema(description = "Описание задачи", example = "Описание задачи на рефакторинг кода")
    @NotNull(message = "Описание не может быть пустым", groups = {OnUpdate.class, OnCreate.class})
    private String description;
    @Schema(description = "Статус задачи", example = "TODO, IN_PROGRESS, DONE")
    private Status status;
    @Schema(description = "Приоритет заказа", example = "HIGH, MIDDLE, LOW")
    private Priority priority;
    @Schema(description = "Имя автора", example = "Владимир")
    private String author;
    @Schema(description = "Имя исполнителя", example = "Иван")
    private String assignee;
}
