package ru.solarev.taskmanagementapi.service.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import ru.solarev.taskmanagementapi.dto.auth.JwtRequest;
import ru.solarev.taskmanagementapi.dto.auth.JwtResponse;
import ru.solarev.taskmanagementapi.entity.user.Role;
import ru.solarev.taskmanagementapi.entity.user.User;
import ru.solarev.taskmanagementapi.exceptions.ResourceNotFoundException;
import ru.solarev.taskmanagementapi.repository.UserRepository;
import ru.solarev.taskmanagementapi.security.CustomUserDetailsService;
import ru.solarev.taskmanagementapi.security.JwtTokenProvider;
import ru.solarev.taskmanagementapi.service.impl.AuthServiceImpl;
import ru.solarev.taskmanagementapi.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login() {
        //Mocking
        Long userId = 1L;
        String username = "username";
        String email = "email";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        JwtRequest request = new JwtRequest();
        request.setEmail(email);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);

        //Test
        Mockito.when(userService.findByEmail(email))
                .thenReturn(user);
        Mockito.when(jwtTokenProvider.generateToken(userDetailsService.loadUserByUsername(email)))
                .thenReturn(accessToken);
        JwtResponse response = authService.login(request);
        Mockito.verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword())
                );
        Assertions.assertEquals(response.getEmail(), username);
        Assertions.assertEquals(response.getId(), userId);
        Assertions.assertNotNull(response.getAccessToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String email = "email";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setEmail(email);
        request.setPassword(password);
        User user = new User();
        user.setEmail(email);
        Mockito.when(userService.findByEmail(email))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(jwtTokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }
}
