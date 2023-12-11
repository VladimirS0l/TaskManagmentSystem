package ru.solarev.taskmanagementapi.service;

import ru.solarev.taskmanagementapi.dto.CommentDto;
import ru.solarev.taskmanagementapi.dto.TaskDto;
import ru.solarev.taskmanagementapi.entity.task.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByTaskId(Long taskId);
    Comment findById(Long id);
    Comment create(Long taskId, Comment comment, String email);
    void delete(Long id, String email);
}
