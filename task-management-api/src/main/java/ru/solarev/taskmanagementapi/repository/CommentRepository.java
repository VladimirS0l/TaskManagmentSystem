package ru.solarev.taskmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.solarev.taskmanagementapi.entity.task.Comment;
import ru.solarev.taskmanagementapi.entity.task.Task;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByTask(Task task);
}
