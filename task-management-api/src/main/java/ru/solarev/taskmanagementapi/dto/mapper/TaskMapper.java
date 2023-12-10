package ru.solarev.taskmanagementapi.dto.mapper;

import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.solarev.taskmanagementapi.dto.TaskDto;
import ru.solarev.taskmanagementapi.entity.task.Task;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper implements Mappable<Task, TaskDto> {

    @Override
    public List<TaskDto> toDto(List<Task> entity) {
        return entity.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDto toDto(Task entity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(entity.getId());
        taskDto.setTitle(entity.getTitle());
        taskDto.setDescription(entity.getDescription());
        taskDto.setStatus(entity.getStatus());
        taskDto.setPriority(entity.getPriority());
        taskDto.setAuthor(entity.getAuthor().getUsername());
        taskDto.setAssignee(entity.getAssignee());
        return taskDto;
    }

    @Override
    public List<Task> toEntity(List<TaskDto> dto) {
        return dto.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Task toEntity(TaskDto dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        return task;
    }
}
