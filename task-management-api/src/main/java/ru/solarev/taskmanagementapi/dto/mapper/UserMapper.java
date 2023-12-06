package ru.solarev.taskmanagementapi.dto.mapper;

import ru.solarev.taskmanagementapi.dto.UserDto;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper implements Mappable<User, UserDto> {
    @Override
    public List<UserDto> toDto(List<User> entity) {
        return entity.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto toDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setUsername(entity.getUsername());
        userDto.setEmail(entity.getEmail());
        userDto.setPassword(userDto.getPassword());
        userDto.setConfirmedPassword(entity.getConfirmedPassword());
        return userDto;
    }

    @Override
    public List<User> toEntity(List<UserDto> dto) {
        return dto.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setConfirmedPassword(dto.getConfirmedPassword());
        return user;
    }
}