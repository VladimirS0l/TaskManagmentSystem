package ru.solarev.taskmanagementapi.dto;

import lombok.Data;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.user.User;

@Data
public class CommentDto {
    private Long id;
    private Task taskComment;
    private User userComment;
    private String message;
}
