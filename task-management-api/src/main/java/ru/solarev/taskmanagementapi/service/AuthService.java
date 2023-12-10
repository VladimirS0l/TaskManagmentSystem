package ru.solarev.taskmanagementapi.service;

import ru.solarev.taskmanagementapi.dto.auth.JwtRequest;
import ru.solarev.taskmanagementapi.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);
}
