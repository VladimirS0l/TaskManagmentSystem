package ru.solarev.taskmanagementapi.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String confirmedPassword;
}
