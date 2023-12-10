package ru.solarev.taskmanagementapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.solarev.taskmanagementapi.dto.auth.JwtRequest;
import ru.solarev.taskmanagementapi.dto.auth.JwtResponse;
import ru.solarev.taskmanagementapi.repository.UserRepository;
import ru.solarev.taskmanagementapi.security.CustomUserDetailsService;
import ru.solarev.taskmanagementapi.security.JwtTokenProvider;
import ru.solarev.taskmanagementapi.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        var jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        UserDetails user = userDetailsService
                .loadUserByUsername(loginRequest.getEmail());
        var userInfo = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow();
        if (user != null) {
            jwtResponse.setId(userInfo.getId());
            jwtResponse.setEmail(userInfo.getEmail());
            jwtResponse.setAccessToken(jwtTokenProvider.generateToken(user));
            return jwtResponse;
        }
        return jwtResponse;
    }
}
