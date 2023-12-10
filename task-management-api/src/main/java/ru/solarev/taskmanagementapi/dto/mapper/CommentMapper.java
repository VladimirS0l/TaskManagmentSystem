package ru.solarev.taskmanagementapi.dto.mapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.solarev.taskmanagementapi.dto.CommentDto;
import ru.solarev.taskmanagementapi.entity.task.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper implements Mappable<Comment, CommentDto> {
    @Override
    public List<CommentDto> toDto(List<Comment> entity) {
        return entity.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto toDto(Comment entity) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(entity.getId());
        commentDto.setUsername(entity.getUsername());
        commentDto.setMessage(entity.getMessage());
        return commentDto;
    }

    @Override
    public List<Comment> toEntity(List<CommentDto> dto) {
        return dto.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Comment toEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setUsername(dto.getUsername());
        comment.setMessage(dto.getMessage());
        return comment;
    }
}
