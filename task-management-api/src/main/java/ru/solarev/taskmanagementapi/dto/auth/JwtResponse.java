package ru.solarev.taskmanagementapi.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private Long id;
    private String email;
    private String accessToken;
}
