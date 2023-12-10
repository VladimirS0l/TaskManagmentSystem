package ru.solarev.taskmanagementapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.solarev.taskmanagementapi.dto.CommentDto;
import ru.solarev.taskmanagementapi.dto.mapper.CommentMapper;
import ru.solarev.taskmanagementapi.dto.validation.OnCreate;
import ru.solarev.taskmanagementapi.dto.validation.OnUpdate;
import ru.solarev.taskmanagementapi.service.CommentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("v1/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Комментарии", description = "Методы для управления комментариями")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("{taskId}/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Посмотреть все комментарии к задаче по ID")
    public List<CommentDto> showAllCommentsByTask(
            @Parameter(description = "Id задачи")
            @PathVariable("taskId") Long id) {
        return commentMapper
                .toDto(commentService.findAllByTaskId(id));
    }

    @PostMapping("{taskId}/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Написать комментарий")
    public CommentDto createComment(
            @Parameter(description = "Id задачи")
            @PathVariable("taskId") Long id,
            @Validated(OnCreate.class) @RequestBody CommentDto commentDto,
            Principal principal) {
        return commentMapper.toDto(commentService.create(
                id, commentMapper.toEntity(commentDto), principal.getName()));
    }

    @DeleteMapping("{commentId}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удалить комментарий по ID")
    public void deleteComment(
            @Parameter(description = "Id комментария")
            @PathVariable("commentId") Long id,
            Principal principal) {
        commentService.delete(id, principal.getName());
    }


}
