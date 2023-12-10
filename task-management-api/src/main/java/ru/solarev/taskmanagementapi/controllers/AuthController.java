package ru.solarev.taskmanagementapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.solarev.taskmanagementapi.dto.UserDto;
import ru.solarev.taskmanagementapi.dto.auth.JwtRequest;
import ru.solarev.taskmanagementapi.dto.auth.JwtResponse;
import ru.solarev.taskmanagementapi.dto.mapper.UserMapper;
import ru.solarev.taskmanagementapi.dto.validation.OnCreate;
import ru.solarev.taskmanagementapi.service.AuthService;
import ru.solarev.taskmanagementapi.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Авторизация", description = "Методы для управления авторизацией пользователя")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Авторизация пользователя")
    public JwtResponse login(@Validated
                             @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Регистрация пользователя")
    public UserDto register(@Validated(OnCreate.class)
                            @RequestBody final UserDto userDto) {
        return userMapper.toDto(userService.create(
                userMapper.toEntity(userDto)));
    }

    @GetMapping("info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Информация об авторизации пользователя")
    public String getInfo(Principal principal) {
        if (principal != null) return "Вход выполнен: " + principal.getName();
        return "Пользователь не авторизовался";
    }

}
