package ru.solarev.taskmanagementapi.service.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.task.enums.Priority;
import ru.solarev.taskmanagementapi.entity.task.enums.Status;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.repository.TaskRepository;
import ru.solarev.taskmanagementapi.service.UserService;
import ru.solarev.taskmanagementapi.service.impl.TaskServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testFindAllTasksAuthor() {
        // Mocking
        String userEmail = "test@example.com";
        Priority priority = Priority.HIGH;
        Integer page = 1;
        Integer pageSize = 10;

        User user = new User();
        user.setEmail(userEmail);

        Page<Task> taskPage = Page.empty();
        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(taskRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(taskPage);

        // Test
        Page<Task> result = taskService.findAllTasksAuthor(userEmail, priority, page, pageSize);

        assertNotNull(result);
        assertEquals(taskPage, result);
    }

    @Test
    void testFindAllTasksByUserId() {
        // Mocking
        Long userId = 1L;
        Integer page = 1;
        Integer pageSize = 10;

        User user = new User();
        user.setId(userId);

        Page<Task> taskPage = Page.empty();
        when(userService.findById(userId)).thenReturn(user);
        when(taskRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(taskPage);

        // Test
        Page<Task> result = taskService.findAllTasksByUserId(userId, page, pageSize);

        assertNotNull(result);
        assertEquals(taskPage, result);
    }

    @Test
    void testFindById() {
        // Mocking
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Test
        Task result = taskService.findById(taskId);

        assertNotNull(result);
        assertEquals(taskId, result.getId());
    }

    @Test
    void testCreate() {
        // Mocking
        String userEmail = "test@example.com";
        User author = new User();
        author.setEmail(userEmail);

        Task newTask = new Task();
        newTask.setTitle("New Task");

        when(userService.findByEmail(userEmail)).thenReturn(author);
        when(taskRepository.save(any())).thenReturn(newTask);

        // Test
        Task result = taskService.create(newTask, userEmail);

        assertNotNull(result);
        assertEquals(author, result.getAuthor());
    }

    @Test
    void testUpdate() {
        // Mocking
        Long taskId = 1L;
        User user = new User();
        String userEmail = "test@example.com";
        user.setEmail(userEmail);
        Task updateTask = new Task();
        updateTask.setId(taskId);
        updateTask.setTitle("Updated Task");
        updateTask.setAuthor(user);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Existing Task");
        existingTask.setAuthor(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any())).thenReturn(updateTask);

        // Test
        Task result = taskService.update(updateTask, userEmail);

        assertNotNull(result);
        assertEquals(updateTask.getTitle(), result.getTitle());
    }

    @Test
    void testDelete() {
        // Mocking
        Long taskId = 1L;
        String userEmail = "test@example.com";
        Task task = new Task();
        task.setId(taskId);
        User user = new User();
        user.setEmail(userEmail);
        task.setAuthor(user);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Test
        assertDoesNotThrow(() -> taskService.delete(taskId, userEmail));
    }

    @Test
    void testUpdateStatus() {
        // Mocking
        Long taskId = 1L;
        Long assigneeId = 2L;
        String userEmail = "test@example.com";
        String userName = "username";
        User user = new User();
        user.setEmail(userEmail);

        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(user);
        task.setAssignee("assignee");
        task.setStatus(Status.IN_PROGRESS);

        User author = new User();
        author.setEmail(userEmail);
        author.setName(userName);

        User assignee = new User();
        assignee.setId(assigneeId);
        assignee.setName(userName);

        when(userService.findByEmail(userEmail)).thenReturn(author);
        when(userService.findById(assigneeId)).thenReturn(assignee);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        // Test
        Task result = taskService.updateStatus(Status.IN_PROGRESS, taskId, assigneeId, userEmail);

        assertNotNull(result);
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void testSetAssignee() {
        // Mocking
        Long taskId = 1L;
        Long assigneeId = 2L;
        String userEmail = "test@example.com";
        User userTest = new User();
        userTest.setEmail(userEmail);

        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(userTest);
        task.setAssignee("oldAssignee");

        User user = new User();
        user.setId(assigneeId);

        when(userService.findById(assigneeId)).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        // Test
        Task result = taskService.setAssignee(assigneeId, taskId, userEmail);

        assertNotNull(result);
        assertEquals(user.getName(), result.getAssignee());
    }
}
