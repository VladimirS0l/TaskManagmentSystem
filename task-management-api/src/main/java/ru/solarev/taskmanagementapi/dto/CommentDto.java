package ru.solarev.taskmanagementapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.solarev.taskmanagementapi.dto.validation.OnCreate;
import ru.solarev.taskmanagementapi.dto.validation.OnUpdate;

@Getter
@Setter
@Schema(description = "Модель комментария")
public class CommentDto {
    @Schema(description = "ID комментария", example = "1")
    @NotNull(message = "ID не может быть пустым", groups = OnUpdate.class)
    private Long id;
    @Schema(description = "Пользователь оставивший комментарий")
    private String username;
    @Schema(description = "Комментарий")
    @NotNull(message = "Комментарий не может быть пустым", groups = {OnUpdate.class, OnCreate.class})
    private String message;
}
