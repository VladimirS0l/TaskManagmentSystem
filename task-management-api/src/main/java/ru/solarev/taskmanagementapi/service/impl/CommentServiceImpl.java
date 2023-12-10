package ru.solarev.taskmanagementapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.solarev.taskmanagementapi.dto.CommentDto;
import ru.solarev.taskmanagementapi.dto.TaskDto;
import ru.solarev.taskmanagementapi.dto.mapper.CommentMapper;
import ru.solarev.taskmanagementapi.entity.task.Comment;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;
import ru.solarev.taskmanagementapi.repository.CommentRepository;
import ru.solarev.taskmanagementapi.repository.TaskRepository;
import ru.solarev.taskmanagementapi.repository.UserRepository;
import ru.solarev.taskmanagementapi.service.CommentService;
import ru.solarev.taskmanagementapi.service.TaskService;
import ru.solarev.taskmanagementapi.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Override
    public List<Comment> findAllByTaskId(Long taskId) {
        var task = taskService.findById(taskId);
        return commentRepository.findAllByTask(task);
    }

    @Override
    public Comment create(Long taskId, Comment comment, String email) {
        var task = taskService.findById(taskId);
        var user = userService.findByEmail(email);
        comment.setId(null);
        comment.setTask(task);
        comment.setUsername(user.getName());
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long id, String email) {
        var user = userService.findByEmail(email);
        var comment =
                commentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Комментарий не найден"));
        if (comment.getUsername().equals(user.getName())) {
            commentRepository.delete(comment);
        }
    }
}
