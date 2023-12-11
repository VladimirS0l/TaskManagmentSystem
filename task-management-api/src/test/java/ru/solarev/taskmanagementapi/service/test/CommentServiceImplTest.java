package ru.solarev.taskmanagementapi.service.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.solarev.taskmanagementapi.entity.task.Comment;
import ru.solarev.taskmanagementapi.entity.task.Task;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;
import ru.solarev.taskmanagementapi.repository.CommentRepository;
import ru.solarev.taskmanagementapi.service.TaskService;
import ru.solarev.taskmanagementapi.service.UserService;
import ru.solarev.taskmanagementapi.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testFindAllByTaskId() {
        // Mocking
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = List.of(comment1, comment2);

        when(taskService.findById(taskId)).thenReturn(task);
        when(commentRepository.findAllByTask(task)).thenReturn(comments);

        // Test
        List<Comment> result = commentService.findAllByTaskId(taskId);

        assertNotNull(result);
        assertEquals(comments, result);
    }

    @Test
    void testFindById() {
        // Mocking
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Test
        Comment result = commentService.findById(commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
    }

    @Test
    void testCreate() {
        // Mocking
        Long taskId = 1L;
        String userEmail = "test@example.com";
        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setEmail(userEmail);

        Comment newComment = new Comment();
        newComment.setMessage("New Comment");

        when(taskService.findById(taskId)).thenReturn(task);
        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(commentRepository.save(any())).thenReturn(newComment);

        // Test
        Comment result = commentService.create(taskId, newComment, userEmail);

        assertNotNull(result);
        assertEquals(task, result.getTask());
        assertEquals(user.getName(), result.getUsername());
    }

    @Test
    void testDelete() {
        // Mocking
        Long commentId = 1L;
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);

        Comment comment = new Comment();
        comment.setId(commentId);

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Test
        assertDoesNotThrow(() -> commentService.delete(commentId, userEmail));
    }

    @Test
    void testDeleteNonExistingComment() {
        // Mocking
        Long commentId = 1L;
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);

        when(userService.findByEmail(userEmail)).thenReturn(user);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Test
        assertThrows(ResourceNotFoundException.class, () -> commentService.delete(commentId, userEmail));
    }
}
