package ru.solarev.taskmanagementapi.dto;

import lombok.Data;
import ru.solarev.taskmanagementapi.entity.task.Comment;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private User author;
    private User performer;
    private List<Comment> comments;
}
