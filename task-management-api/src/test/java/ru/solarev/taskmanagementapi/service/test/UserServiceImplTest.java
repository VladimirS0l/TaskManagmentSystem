package ru.solarev.taskmanagementapi.service.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.solarev.taskmanagementapi.entity.user.Role;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.repository.UserRepository;
import ru.solarev.taskmanagementapi.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    UserServiceImplTest() {
    }

    @Test
    void testFindAll() {
        // Mocking
        List<User> userList = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(userList);

        // Test
        List<User> result = userService.findAll();

        assertEquals(userList, result);
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void testFindByEmail() {
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(userEmail);

        assertNotNull(result);
        assertEquals(userEmail, result.getEmail());
    }

    @Test
    void testFindByName() {
        String userName = "testuser";
        User user = new User();
        user.setName(userName);

        when(userRepository.findByName(userName)).thenReturn(Optional.of(user));

        User result = userService.findByName(userName);

        assertNotNull(result);
        assertEquals(userName, result.getName());
    }

    @Test
    void testCreate() {
        // Mocking
        User newUser = new User();
        newUser.setName("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");
        newUser.setConfirmedPassword("password");

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByName(newUser.getUsername())).thenReturn(Optional.empty());
    }

    @Test
    void testUpdate() {
        // Mocking
        Long userId = 1L;
        String userEmail = "test@example.com";

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setName("updatedUser");
        updateUser.setEmail(userEmail);
        updateUser.setPassword("newPassword");
        updateUser.setConfirmedPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("existingUser");
        existingUser.setEmail(userEmail);
        existingUser.setPassword("oldPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(updateUser.getPassword()).thenReturn("encodedNewPassword");

        // Test
        User result = userService.update(updateUser, userEmail);

        assertNotNull(result);
        assertEquals("updatedUser", result.getName());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals(existingUser.getRoles(), result.getRoles());
    }

    @Test
    void testUpdateNonMatchingPasswords() {
        // Mocking
        Long userId = 1L;
        String userEmail = "test@example.com";

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setName("updatedUser");
        updateUser.setEmail(userEmail);
        updateUser.setPassword("newPassword");
        updateUser.setConfirmedPassword("nonMatchingPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(updateUser));

        // Test
        assertThrows(IllegalStateException.class, () -> userService.update(updateUser, userEmail));
    }

    @Test
    void testDelete() {
        // Mocking
        Long userId = 1L;
        String userEmail = "test@example.com";
        User user = new User();
        user.setId(userId);
        user.setEmail(userEmail);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Test
        assertDoesNotThrow(() -> userService.delete(userId, userEmail));
    }

}
