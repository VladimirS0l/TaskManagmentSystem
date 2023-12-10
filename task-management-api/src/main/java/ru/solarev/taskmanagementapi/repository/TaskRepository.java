package ru.solarev.taskmanagementapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByAuthor(User user);
}
