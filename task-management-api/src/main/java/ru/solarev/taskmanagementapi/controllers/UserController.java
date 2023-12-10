package ru.solarev.taskmanagementapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.solarev.taskmanagementapi.dto.UserDto;
import ru.solarev.taskmanagementapi.dto.mapper.UserMapper;
import ru.solarev.taskmanagementapi.dto.validation.OnUpdate;
import ru.solarev.taskmanagementapi.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Пользователи", description = "Методы для управления данными пользователя")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на получение всех пользователей")
    public List<UserDto> getAllUsers() {
        return userMapper.toDto(userService.findAll());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Запрос на получение пользователя по ID")
    public UserDto getUserById(@Parameter(description = "Id пользователя")
                                   @PathVariable("id") Long id) {
        return userMapper.toDto(userService.findById(id));
    }

    @PutMapping("update")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить пользователя")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto userDto,
                              Principal principal) {
        return userMapper.toDto(
                userService.update(userMapper.toEntity(userDto),
                        principal.getName()));
    }

    @DeleteMapping("{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удалить пользователя по ID")
    public void deleteUser(@Parameter(description = "Id пользователя")
                               @PathVariable("id") Long id, Principal principal) {
        userService.delete(id, principal.getName());
    }

    @GetMapping("{id}/addAdmin")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(value = "ADMIN")
    @Operation(summary = "Выдача прав администратора")
    public UserDto addUserAdmin(@Parameter(description = "Id пользователя")
                                    @PathVariable("id") Long id) {
        return userMapper.toDto(userService.addUserAdminRole(id));
    }
}
